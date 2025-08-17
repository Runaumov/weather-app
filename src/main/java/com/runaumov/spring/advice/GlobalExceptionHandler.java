package com.runaumov.spring.advice;

import com.runaumov.spring.exception.*;
import com.runaumov.spring.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({IllegalArgumentException.class, SessionNotFoundException.class})
    public String handleSessionNotFound(
            HttpServletResponse response
    ) {
        CookieUtil.deleteCookie(response);
        return "redirect:/login";
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ModelAndView handleUserNotFound(UserNotFoundException exception) {
        logger.warn("UserNotFoundException: {}", exception.getMessage(), exception);
        return buildErrorPage(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler({AuthenticationFailedException.class})
    public ModelAndView authenticationFailed(AuthenticationFailedException exception) {
        logger.warn("AuthenticationFailedException: {}", exception.getMessage(), exception);
        return buildErrorPage(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler({RegistrationFailedException.class})
    public ModelAndView registrationFailed(RegistrationFailedException exception) {
        logger.warn("RegistrationFailedException: {}", exception.getMessage(), exception);
        return buildErrorPage(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({WeatherApiException.class})
    public ModelAndView handleWeatherApiError(WeatherApiException exception) {
        logger.warn("WeatherApiException: {}", exception.getMessage(), exception);
        return buildErrorPage(HttpStatus.BAD_GATEWAY, exception.getMessage());
    }

    @ExceptionHandler({WeatherApiRequestException.class})
    public ModelAndView weatherApiRequestError(WeatherApiRequestException exception) {
        logger.warn("WeatherApiException: {}", exception.getMessage(), exception);
        return buildErrorPage(HttpStatus.BAD_GATEWAY, exception.getMessage());
    }

    private ModelAndView buildErrorPage(HttpStatus httpStatus, String errorMessage) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(httpStatus);
        modelAndView.addObject("status", httpStatus.value());
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }
}
