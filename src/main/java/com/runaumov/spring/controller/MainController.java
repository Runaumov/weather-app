package com.runaumov.spring.controller;

import com.runaumov.spring.dto.*;
import com.runaumov.spring.service.LocationService;
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
    private final LocationService locationService;

    @Autowired
    public MainController(WeatherService weatherService, UserSessionService userSessionService, LocationService locationService) {
        this.weatherService = weatherService;
        this.userSessionService = userSessionService;
        this.locationService = locationService;
    }

    @GetMapping("/")
    public String indexPage(
            @CookieValue(value = "SESSION_TOKEN", required = false) String sessionToken,
            Model model) {

        Long userId = userSessionService.getUserIdByUserSessionId(UUID.fromString(sessionToken));
        List<LocationDto> locationList = locationService.getLocationDto(userId);

        List<WeatherDto> weatherList = weatherService.getWeatherList(locationList);

        model.addAttribute("weatherList", weatherList);

        return "index";
    }

    @PostMapping("/search")
    public String getWeatherCards(@RequestParam("cityName") String cityName, Model model) {

        CityNameRequest cityNameRequest = new CityNameRequest(cityName);
        List<CityDto> cities = weatherService.getCitiesList(cityNameRequest);

        model.addAttribute("cities", cities);

        return "index";
    }

    @PostMapping("/add")
    public String addLocation(
            @ModelAttribute LocationRequest locationRequest,
            @CookieValue(value = "SESSION_TOKEN", required = false) String sessionToken) {

        LocationDto locationDto = new LocationDto(
                userSessionService.getUserIdByUserSessionId(UUID.fromString(sessionToken)),
                locationRequest.getName(),
                new BigDecimal(locationRequest.getLatitude()),
                new BigDecimal(locationRequest.getLongitude())
        );

        locationService.addLocation(locationDto);

        return "redirect:/";
    }

    @DeleteMapping("/delete")
    public String deleteLocation(@RequestParam("locationId") Long locationId) {
        System.out.println("DELETE method called with locationId: " + locationId);
        locationService.deleteById(locationId);
        return "redirect:/";
    }
}
