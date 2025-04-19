package com.runaumov.spring.controller;

import com.runaumov.spring.dto.CityNameRequest;
import com.runaumov.spring.entity.City;
import com.runaumov.spring.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    private final WeatherService weatherService;

    @Autowired
    public MainController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String indexPage(
            @CookieValue(value = "SESSION_TOKEN", required = false) String sessionToken,
            @CookieValue(value = "username", required = false) String username,
            Model model) {

        model.addAttribute("username", username);
        return "index";
    }

    @PostMapping("/search")
    public String getWeatherCards(
            @RequestParam("cityName") String cityName,
            Model model) {

        CityNameRequest cityNameRequest = new CityNameRequest(cityName);
        List<City> cities = weatherService.getCitiesList(cityNameRequest);

        model.addAttribute("cities", cities);
        return "index";
    }
}
