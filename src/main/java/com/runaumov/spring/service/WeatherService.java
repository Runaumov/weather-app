package com.runaumov.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runaumov.spring.api.WeatherApiClient;
import com.runaumov.spring.dto.CityNameRequest;
import com.runaumov.spring.dto.WeatherDto;
import com.runaumov.spring.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WeatherService {

    @Autowired
    private WeatherApiClient weatherApiClient;
    @Autowired
    private ObjectMapper objectMapper;

    public List<City> getCitiesList(CityNameRequest cityNameRequest) {
        try {
//            String jsonResponse = weatherApiClient.getCityJson(cityNameRequest.getCityName());
//            return objectMapper.readValue(jsonResponse, new TypeReference<List<City>>() {});
            return objectMapper.readValue(
                    weatherApiClient.getCityJson(cityNameRequest.getCityName()),
                    new TypeReference<List<City>>() {
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("!Ошибка в преобразовании json"); // TODO
        }
    }

    public List<WeatherDto> getWeatherList(int userId) {
        return null;
    }
}
