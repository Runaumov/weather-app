package com.runaumov.spring.advice;

import com.runaumov.spring.controller.MainController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {MainController.class})
public class UsernameModelAttributeAdvice {

    @ModelAttribute
    public void addUsernameInModel(
            @CookieValue(value = "username", required = false) String username,
            Model model) {
        model.addAttribute("username", username);
    }
}
