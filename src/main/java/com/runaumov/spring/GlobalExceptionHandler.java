package com.runaumov.spring;

import com.runaumov.spring.exception.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler ({IllegalArgumentException.class, SessionNotFoundException.class})
    public String handleSessionNotFound (
            SessionNotFoundException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Cookie expiredCookie = new Cookie("SESSION_TOKEN", null);
        expiredCookie.setMaxAge(0);
        expiredCookie.setPath("/");
        response.addCookie(expiredCookie);

        return "redirect:/login";
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ModelAndView handleUserNotFound(UserNotFoundException exception) {
        return buildErrorPage(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler({AuthenticationFailedException.class}) // TODO: наверное стоит по-другому обработать это исключение, не через дефолтную страницу
    public ModelAndView authenticationFailed(AuthenticationFailedException exception) {
        return buildErrorPage(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler({RegistrationFailedException.class}) // TODO: наверное стоит по-другому обработать это исключение, не через дефолтную страницу
    public ModelAndView registrationFailed(RegistrationFailedException exception) {
        return buildErrorPage(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({WeatherApiException.class})
    public ModelAndView handleWeatherApiError(WeatherApiException exception) {
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
