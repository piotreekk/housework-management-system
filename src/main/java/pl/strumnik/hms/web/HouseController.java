package pl.strumnik.hms.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.strumnik.hms.domain.*;
import pl.strumnik.hms.exception.AppBusinessException;
import pl.strumnik.hms.repository.HouseInhabitantRepository;
import pl.strumnik.hms.repository.HouseRepository;
import pl.strumnik.hms.repository.HouseworkRepository;
import pl.strumnik.hms.repository.UserAccountRepository;
import pl.strumnik.hms.security.SecurityUtils;
import pl.strumnik.hms.web.model.HouseForm;
import pl.strumnik.hms.web.util.FlashUtil;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping(value = "/house")
@Controller
public class HouseController {

    private final HouseRepository houseRepository;
    private final HouseworkRepository houseworkRepository;
    private final UserAccountRepository userAccountRepository;
    private final HouseInhabitantRepository inhabitantRepository;
    private final FlashUtil flashUtil;

    @Transactional(readOnly = true)
    @GetMapping
    public String houseList(Model model) {
        model.addAttribute("activePage", "houses");

        final Set<House> houses = new HashSet<>();

        final Long loggedUserId = SecurityUtils.getCurrentUserId();
        if (SecurityUtils.hasLoggedUserRole(RoleName.ADMIN)) {
            final Set<House> ownedHouses = houseRepository.findAllByOwnerId(loggedUserId);
            houses.addAll(ownedHouses);
        }
        if (SecurityUtils.hasLoggedUserRole(RoleName.USER)) {
            final Set<House> ownedHouses = houseRepository.findAllByInhabitantId(loggedUserId);
            houses.addAll(ownedHouses);
        }

        model.addAttribute("houses", houses);
        return "house/list";
    }

    @Transactional(readOnly = true)
    @GetMapping("/add")
    public String addHouse(Model model) {
        model.addAttribute("activePage", "houses");
        model.addAttribute("houseForm", new HouseForm());
        return "house/manage";
    }

    @Transactional
    @PostMapping(params = "action=save")
    public String performAddHouse(@ModelAttribute("houseForm") @Validated(HouseForm.Save.class) HouseForm houseForm,
                                  BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "houses");

        if (bindingResult.hasErrors()) {
            model.addAttribute("houseForm", houseForm);
            return "house/manage";
        }

        final Long loggedUserId = SecurityUtils.getCurrentUserId();
        final UserAccount userAccount = userAccountRepository.findById(loggedUserId)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210703224059", UserAccount.UserAccountErrorCode.USER_ACCOUNT_NOT_FOUND));

        final House house = House.create(houseForm.getName(), houseForm.getDescription(), houseForm.getAddressLine1(),
                houseForm.getAddressLine2(), userAccount);

        houseRepository.save(house);

        flashUtil.addSuccessMessage(redirectAttributes, "house.added.message");

        return "redirect:/house";
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public String editHouse(@PathVariable Long id, Model model) {
        model.addAttribute("activePage", "houses");

        final House house = houseRepository.findById(id)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210629220505", House.HouseErrorCode.HOUSE_NOT_FOUND));

        model.addAttribute("houseForm", new HouseForm(house.getId(), house.getName(), house.getDescription(),
                house.getAddressLine1(), house.getAddressLine2()));

        return "house/manage";
    }

    @Transactional
    @PostMapping(params = "action=update")
    public String performEditHouse(@ModelAttribute("houseForm") @Validated(HouseForm.Update.class) HouseForm houseForm,
                                   BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "houses");

        if (bindingResult.hasErrors()) {
            model.addAttribute("houseForm", houseForm);
            return "house/manage";
        }

        final House house = houseRepository.findById(houseForm.getId())
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210629221603", House.HouseErrorCode.HOUSE_NOT_FOUND));

        house.update(houseForm.getName(), house.getDescription(), house.getAddressLine1(), houseForm.getAddressLine2());

        flashUtil.addSuccessMessage(redirectAttributes, "house.updated.message");

        return "redirect:/house";
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}/houseworks")
    public String houseworksList(@PathVariable("id") final Long houseId, Model model) {
        model.addAttribute("activePage", "houses");

        final House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210703160748", House.HouseErrorCode.HOUSE_NOT_FOUND));
        final Set<Housework> houseworks = houseworkRepository.findAllByHouseId_01(houseId);

        model.addAttribute("house", house);
        model.addAttribute("houseworks", houseworks);
        return "housework/list";
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}/inhabitants")
    public String inhabitantsList(@PathVariable("id") final Long houseId, Model model) {
        model.addAttribute("activePage", "houses");

        final House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210703204411", House.HouseErrorCode.HOUSE_NOT_FOUND));
        final Set<HouseInhabitant> inhabitants = inhabitantRepository.findAllByHouseId(houseId);

        model.addAttribute("house", house);
        model.addAttribute("inhabitants", inhabitants);
        return "inhabitant/list";
    }
}
