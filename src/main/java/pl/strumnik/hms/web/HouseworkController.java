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
import pl.strumnik.hms.domain.Housework;
import pl.strumnik.hms.domain.UserAccount;
import pl.strumnik.hms.exception.AppBusinessException;
import pl.strumnik.hms.repository.HouseRepository;
import pl.strumnik.hms.repository.HouseworkRepository;
import pl.strumnik.hms.repository.UserAccountRepository;
import pl.strumnik.hms.security.SecurityUtils;
import pl.strumnik.hms.web.model.HouseworkForm;
import pl.strumnik.hms.web.util.FlashUtil;

@RequiredArgsConstructor
@RequestMapping(value = "/housework")
@Controller
public class HouseworkController {

    private final HouseRepository houseRepository;
    private final HouseworkRepository houseworkRepository;
    private final UserAccountRepository userAccountRepository;
    private final FlashUtil flashUtil;

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public String houseworkDetails(@PathVariable("id") final Long id, Model model) {
        final Housework housework = houseworkRepository.findById(id)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210703201734", Housework.HouseworkErrorCode.HOUSEWORK_NOT_FOUND));

        model.addAttribute("activePage", "houses");
        model.addAttribute("houseId", housework.getHouse().getId());
        model.addAttribute("houseworkForm", new HouseworkForm(housework.getId(), housework.getName(),
                housework.getDescription(), housework.getScheduledAt(), housework.getHouse().getId(),
                housework.getStatus().name(), housework.getFinishedAt(), housework.getExecutorComment()));
        return "housework/manage";
    }

    @Transactional(readOnly = true)
    @GetMapping("/add")
    public String addHousework(@RequestParam("houseId") final Long houseId, Model model) {
        model.addAttribute("activePage", "houses");
        model.addAttribute("houseId", houseId);
        model.addAttribute("houseworkForm", new HouseworkForm());
        return "housework/manage";
    }

    @Transactional
    @PostMapping(params = "action=save")
    public String performAddHousework(@ModelAttribute("houseworkForm") @Validated(HouseworkForm.Save.class) HouseworkForm houseworkForm,
                                      BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "houses");
        if (bindingResult.hasErrors()) {
            model.addAttribute("houseId", houseworkForm.getHouseId());
            model.addAttribute("houseworkForm", houseworkForm);
            return "housework/manage";
        }

        final House house = houseRepository.findById(houseworkForm.getHouseId())
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210703155911", House.HouseErrorCode.HOUSE_NOT_FOUND));

        final Housework housework = Housework.create(houseworkForm.getName(), houseworkForm.getDescription(), houseworkForm.getScheduledAt(),
                house, null);

        houseworkRepository.save(housework);

        flashUtil.addSuccessMessage(redirectAttributes, "house.housework.added.message");

        return "redirect:/house/" + house.getId() + "/houseworks";
    }

    @Transactional
    @PostMapping(params = "action=update")
    public String performUpdateHousework(@ModelAttribute("houseworkForm") @Validated(HouseworkForm.Update.class) HouseworkForm houseworkForm,
                                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "houses");
        if (bindingResult.hasErrors()) {
            model.addAttribute("houseId", houseworkForm.getHouseId());
            model.addAttribute("houseworkForm", houseworkForm);
            return "housework/manage";
        }

        final Housework housework = houseworkRepository.findById(houseworkForm.getId())
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210705222920", Housework.HouseworkErrorCode.HOUSEWORK_NOT_FOUND));

        housework.update(houseworkForm.getName(), houseworkForm.getDescription(), houseworkForm.getScheduledAt());

        flashUtil.addSuccessMessage(redirectAttributes, "house.housework.updated.message");

        return "redirect:/housework/" + housework.getId();
    }

    @Transactional
    @GetMapping("/{id}/assign")
    public String assignHousework(@PathVariable final Long id, RedirectAttributes redirectAttributes) {
        final Housework housework = houseworkRepository.findById(id)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210704215859", Housework.HouseworkErrorCode.HOUSEWORK_NOT_FOUND));

        final Long loggedUserId = SecurityUtils.getCurrentUserId();
        final UserAccount user = userAccountRepository.findByIdWithHouseworks(loggedUserId)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210704215952", UserAccount.UserAccountErrorCode.USER_ACCOUNT_NOT_FOUND));

        housework.assignHousework(user);

        flashUtil.addSuccessMessage(redirectAttributes, "house.housework.assigned.message");

        return "redirect:/housework/" + housework.getId();
    }

    @Transactional
    @GetMapping("/{id}/resign")
    public String resignHousework(@PathVariable final Long id, RedirectAttributes redirectAttributes) {
        final Housework housework = houseworkRepository.findById(id)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210704221649", Housework.HouseworkErrorCode.HOUSEWORK_NOT_FOUND));

        final Long loggedUserId = SecurityUtils.getCurrentUserId();
        final UserAccount user = userAccountRepository.findByIdWithHouseworks(loggedUserId)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210704221721", UserAccount.UserAccountErrorCode.USER_ACCOUNT_NOT_FOUND));

        housework.resignFromHousework(user);

        flashUtil.addSuccessMessage(redirectAttributes, "house.housework.resigned.message");

        return "redirect:/housework/" + housework.getId();
    }

    @Transactional
    @GetMapping("/{id}/finish")
    public String finishHousework(@PathVariable final Long id, RedirectAttributes redirectAttributes) {
        final Housework housework = houseworkRepository.findByIdWithUser(id)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210704221937", Housework.HouseworkErrorCode.HOUSEWORK_NOT_FOUND));

        final Long loggedUserId = SecurityUtils.getCurrentUserId();
        final UserAccount user = userAccountRepository.findById(loggedUserId)
                .orElseThrow(() -> new AppBusinessException("SCIN_PS_20210704221940", UserAccount.UserAccountErrorCode.USER_ACCOUNT_NOT_FOUND));

        housework.finishHousework(user, "Wykonałem zadanie wzorowo");

        flashUtil.addSuccessMessage(redirectAttributes, "house.housework.finished.message");

        return "redirect:/housework/" + housework.getId();
    }

}
