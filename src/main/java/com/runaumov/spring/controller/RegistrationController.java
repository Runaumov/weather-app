package com.runaumov.spring.controller;

import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserDto;
import com.runaumov.spring.dto.UserRegistrationRequest;
import com.runaumov.spring.dto.UserSessionDto;
import com.runaumov.spring.service.UserService;
import com.runaumov.spring.service.UserSessionService;
import com.runaumov.spring.dao.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final UserSessionService userSessionService;

    @Autowired
    public RegistrationController(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    @GetMapping
    public String showRegistrationPage(Model model) {
        model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
        return "registration";
    }

    @PostMapping
    public String registrationUser(
            @ModelAttribute("userRegistrationRequestDto") UserRegistrationRequest userRegistrationRequest,
            HttpServletResponse response) {

        UserDto userDto = new UserDto(userRegistrationRequest.getLogin(), userRegistrationRequest.getPassword());
        UserAuthenticatedDto userAuthenticatedDto = userService.registerNewUser(userDto);
        UserSessionDto userSessionDto = userSessionService.createNewUserSession(userAuthenticatedDto);

        CookieUtil.setSessionCookie(response, userSessionDto);
        return "redirect:/";
    }
}
