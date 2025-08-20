package com.runaumov.spring.controller;

import com.runaumov.spring.service.UserSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private final UserSessionService userSessionService;

    @Autowired
    public LogoutController(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @GetMapping
    public String logoutUser(
            @CookieValue(value = "SESSION_TOKEN", required = false) String sessionToken,
            HttpServletResponse response) {

        if (sessionToken != null) {
            userSessionService.removeSession(UUID.fromString(sessionToken));

            Cookie cookie = new Cookie("SESSION_TOKEN", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        return "redirect:/login";
    }
}
