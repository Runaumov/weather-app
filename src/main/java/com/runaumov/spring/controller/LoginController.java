package com.runaumov.spring.controller;

import com.runaumov.spring.dto.AuthenticatedUserDto;
import com.runaumov.spring.dto.UserDto;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.entity.UserSession;
import com.runaumov.spring.service.SessionManagerService;
import com.runaumov.spring.service.UserCheckService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserCheckService userCheckService;
    private final SessionManagerService sessionManagerService;

    @Autowired
    public LoginController(UserCheckService userCheckService, SessionManagerService sessionManagerService) {
        this.userCheckService = userCheckService;
        this.sessionManagerService = sessionManagerService;
    }

    @GetMapping
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping
    public String loginUser(
            @ModelAttribute("User") User user,
            @CookieValue(value = "SESSION_TOKEN", required = false) String sessionToken,
            HttpServletResponse response) {

            // TODO : добавить маппинг
            UserDto userDto = new UserDto(user.getLogin(), user.getPassword());
            AuthenticatedUserDto authenticatedUserDto = userCheckService.getAuthenticatedUserDto(userDto);

            UserSession userSession = sessionManagerService.createNewUserSession(authenticatedUserDto);
            String newSessionToken = userSession.getId().toString();

            Cookie sessionCookie = new Cookie("SESSION_TOKEN", newSessionToken);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);

            return "redirect:/";
    }
}
