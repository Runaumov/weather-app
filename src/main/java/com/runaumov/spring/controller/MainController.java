package com.runaumov.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String indexPage(
            @CookieValue(value = "SESSION_TOKEN", required = false) String sessionToken,
            @CookieValue(value = "username", required = false) String username,
            Model model) {

        model.addAttribute("username", username);
        return "index";
    }
}
