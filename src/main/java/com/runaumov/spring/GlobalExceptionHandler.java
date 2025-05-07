package com.runaumov.spring;

import com.runaumov.spring.exception.SessionNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
