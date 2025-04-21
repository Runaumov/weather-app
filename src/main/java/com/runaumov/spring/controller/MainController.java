package com.runaumov.spring.controller;

import com.runaumov.spring.dto.CityNameRequest;
import com.runaumov.spring.dto.LocationRequest;
import com.runaumov.spring.dto.UserLocationDto;
import com.runaumov.spring.entity.City;
import com.runaumov.spring.entity.UserSession;
import com.runaumov.spring.service.UserSessionService;
import com.runaumov.spring.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    private final WeatherService weatherService;
    private final UserSessionService userSessionService;

    @Autowired
    public MainController(WeatherService weatherService, UserSessionService userSessionService) {
        this.weatherService = weatherService;
        this.userSessionService = userSessionService;
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

    @PostMapping("/add")
    public String addLocation(
            @ModelAttribute LocationRequest locationRequest,
            @CookieValue (value = "SESSION_TOKEN", required = false) String sessionToken) {

        int userId = userSessionService.getUserIdByUserSessionId(UUID.fromString(sessionToken));
        BigDecimal latitude = new BigDecimal(locationRequest.getLatitude());
        BigDecimal longitude = new BigDecimal(locationRequest.getLongitude());
        UserLocationDto userLocationDto = new UserLocationDto(userId, latitude, longitude);

        return "redirect:/";
    }
}
