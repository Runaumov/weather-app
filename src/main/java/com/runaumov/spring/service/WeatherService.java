package com.runaumov.spring.service;

import com.runaumov.spring.api.WeatherApiClient;
import com.runaumov.spring.dto.CityDto;
import com.runaumov.spring.dto.CityNameRequest;
import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.dto.WeatherDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final WeatherApiClient weatherApiClient;

    @Autowired
    public WeatherService(WeatherApiClient weatherApiClient) {
                this.weatherApiClient = weatherApiClient;
    }

    public List<CityDto> getCitiesList(CityNameRequest cityNameRequest) {
        return weatherApiClient.getCitiesList(cityNameRequest.getCityName());
    }

    public List<WeatherDto> getWeatherList(List<LocationDto> locationList) {
        List<WeatherDto> weatherDtoList = new ArrayList<>();
        for (LocationDto locationDto : locationList) {
            weatherDtoList.add(weatherApiClient.getWeatherDto(locationDto));
        }
        return weatherDtoList;
    }
}
