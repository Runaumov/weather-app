package com.runaumov.spring.controller;

import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserDto;
import com.runaumov.spring.dto.UserSessionDto;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.service.UserSessionService;
import com.runaumov.spring.service.UserService;
import com.runaumov.spring.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final UserSessionService userSessionService;

    @Autowired
    public LoginController(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    @GetMapping
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping
    public String loginUser(
            @ModelAttribute("User") User user,
            HttpServletResponse response) {

            // TODO : добавить маппинг
            UserDto userDto = new UserDto(user.getLogin(), user.getPassword());
            UserAuthenticatedDto userAuthenticatedDto = userService.getAuthenticatedUserDto(userDto);
            UserSessionDto userSessionDto = userSessionService.createNewUserSession(userAuthenticatedDto);

            CookieUtil.setSessionCookie(response, userSessionDto);
            return "redirect:/";
    }
}
