package com.runaumov.spring.controller;
import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserDto;
import com.runaumov.spring.dto.UserRegistrationRequest;
import com.runaumov.spring.dto.UserSessionDto;
import com.runaumov.spring.service.UserSessionService;
import com.runaumov.spring.service.UserService;
import jakarta.servlet.http.Cookie;
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
        model.addAttribute("userRegistrationRequestDto", new UserRegistrationRequest());
        return "registration";
    }

    @PostMapping
    public String registrationUser(
            @ModelAttribute("userRegistrationRequestDto") UserRegistrationRequest userRegistrationRequest,
            HttpServletResponse response) {

        String login = userRegistrationRequest.getLogin();
        UserAuthenticatedDto userAuthenticatedDto = new UserAuthenticatedDto();

        if (!userService.isUserExist(login)) {
            UserDto userDto = new UserDto(login, userRegistrationRequest.getPassword());
            userAuthenticatedDto = userService.registerNewUser(userDto);
        }

        UserSessionDto userSessionDto = userSessionService.createNewUserSession(userAuthenticatedDto); // TODO : дублирование
        String newSessionToken = userSessionDto.getSessionId().toString();
        String username = userSessionDto.getUserLogin();

        Cookie sessionCookie = new Cookie("SESSION_TOKEN", newSessionToken);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");

        Cookie usernameCookie = new Cookie("username", username);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");

        response.addCookie(sessionCookie);
        response.addCookie(usernameCookie);

        return "redirect:/";
    }
}
