package pl.strumnik.hms.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.strumnik.hms.domain.House;
import pl.strumnik.hms.domain.HouseInhabitant;
import pl.strumnik.hms.domain.UserAccount;
import pl.strumnik.hms.exception.AppBusinessException;
import pl.strumnik.hms.repository.HouseInhabitantRepository;
import pl.strumnik.hms.repository.HouseRepository;
import pl.strumnik.hms.repository.UserAccountRepository;
import pl.strumnik.hms.web.model.HouseInhabitantForm;
import pl.strumnik.hms.web.util.FlashUtil;

import java.time.LocalDate;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping(value = "/inhabitant")
@Controller
public class HouseInhabitantController {

    private final HouseRepository houseRepository;
    private final UserAccountRepository userAccountRepository;
    private final HouseInhabitantRepository houseInhabitantRepository;
    private final FlashUtil flashUtil;

    @Transactional(readOnly = true)
    @GetMapping("/add")
    public String addInhabitant(@RequestParam Long houseId, Model model) {
        model.addAttribute("activePage", "houses");

        final Set<UserAccount> candidatesToHouseInhabitants = userAccountRepository.findCandidatesToHouseInhabitants(houseId);

        model.addAttribute("candidateUsers", candidatesToHouseInhabitants);
        model.addAttribute("inhabitantForm", new HouseInhabitantForm(null, houseId, null, LocalDate.now(), null));
        return "inhabitant/manage";
    }

    @Transactional
    @PostMapping(params = "action=save")
    public String performAddInhabitant(@ModelAttribute("inhabitantForm") @Validated(HouseInhabitantForm.Save.class) HouseInhabitantForm inhabitantForm,
                                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "houses");

        if (bindingResult.hasErrors()) {
            model.addAttribute("inhabitantForm", inhabitantForm);
            return "inhabitant/manage";
        }

        final House house = houseRepository.findById(inhabitantForm.getHouseId())
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210703232133", House.HouseErrorCode.HOUSE_NOT_FOUND));

        final UserAccount user = userAccountRepository.findById(inhabitantForm.getUserId())
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210703232246", UserAccount.UserAccountErrorCode.USER_ACCOUNT_NOT_FOUND));

        HouseInhabitant.create(inhabitantForm.getDateFrom(), inhabitantForm.getDateTo(), user, house);

        flashUtil.addSuccessMessage(redirectAttributes, "house.inhabitant.added.message");

        return "redirect:/house/" + house.getId() + "/inhabitants";
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public String inhabitantDetails(@PathVariable Long id, Model model) {
        model.addAttribute("activePage", "houses");

        final HouseInhabitant houseInhabitant = houseInhabitantRepository.findByIdWithUser(id)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210704140310", HouseInhabitant.HouseInhabitantErrorCode.HOUSE_INHABITANT_NOT_FOUND));

        final Long houseId = houseInhabitant.getHouse().getId();
        final Long userId = houseInhabitant.getUser().getId();

        final Set<UserAccount> candidatesToHouseInhabitants = Set.of(houseInhabitant.getUser());

        model.addAttribute("candidateUsers", candidatesToHouseInhabitants);
        model.addAttribute("inhabitantForm", new HouseInhabitantForm(houseInhabitant.getId(), houseId, userId,
                houseInhabitant.getDateFrom(), houseInhabitant.getDateTo()));

        return "inhabitant/manage";
    }

    @Transactional
    @PostMapping(params = "action=update")
    public String performUpdateInhabitant(@ModelAttribute("inhabitantForm") @Validated(HouseInhabitantForm.Update.class) HouseInhabitantForm inhabitantForm,
                                          BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "houses");

        if (bindingResult.hasErrors()) {
            model.addAttribute("inhabitantForm", inhabitantForm);
            return "inhabitant/manage";
        }

        final HouseInhabitant houseInhabitant = houseInhabitantRepository.findById(inhabitantForm.getId())
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210704141954", HouseInhabitant.HouseInhabitantErrorCode.HOUSE_INHABITANT_NOT_FOUND));

        houseInhabitant.update(inhabitantForm.getDateFrom(), inhabitantForm.getDateTo());

        flashUtil.addSuccessMessage(redirectAttributes, "house.inhabitant.updated.message");

        return "redirect:/house/" + inhabitantForm.getHouseId() + "/inhabitants";
    }

}
