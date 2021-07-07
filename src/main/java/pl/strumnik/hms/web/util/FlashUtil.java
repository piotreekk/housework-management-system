package pl.strumnik.hms.web.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Component
public class FlashUtil {

    private final MessageSource messageSource;

    public void addSuccessMessage(RedirectAttributes redirectAttributes, String messageKey, Object... params) {
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage(messageKey, params, LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("className", "alert alert-success");
    }

    public void addErrorMessage(RedirectAttributes redirectAttributes, String messageKey, Object... params) {
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage(messageKey, params, LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("className", "alert alert-error");
    }
}
