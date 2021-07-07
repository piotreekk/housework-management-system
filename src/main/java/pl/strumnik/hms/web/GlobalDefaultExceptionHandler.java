package pl.strumnik.hms.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import pl.strumnik.hms.exception.AppBaseRuntimeException;
import pl.strumnik.hms.exception.AppBusinessException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "general_error";

    @ExceptionHandler(value = AppBaseRuntimeException.class)
    public ModelAndView businessExceptionHandler(HttpServletRequest req, AppBusinessException e) {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}
