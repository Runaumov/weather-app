package com.runaumov.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runaumov.spring.api.WeatherApiClient;
import com.runaumov.spring.dto.CityNameRequest;
import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.dto.WeatherDto;
import com.runaumov.spring.dto.CityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final WeatherApiClient weatherApiClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherService(ObjectMapper objectMapper, WeatherApiClient weatherApiClient) {
        this.objectMapper = objectMapper;
        this.weatherApiClient = weatherApiClient;
    }

    public List<CityDto> getCitiesList(CityNameRequest cityNameRequest) {
        return parseJson(
                weatherApiClient.getCityJson(cityNameRequest.getCityName()),
                new TypeReference<List<CityDto>>() {});
    }

    public List<WeatherDto> getWeatherList(List<LocationDto> locationList) {
        List<WeatherDto> weatherDtoList = new ArrayList<>();
        for (LocationDto locationDto : locationList) {
            try {
                String weatherJson = weatherApiClient.getWeatherJson(locationDto);
                WeatherDto weatherDto = objectMapper.readValue(weatherJson, WeatherDto.class);
                weatherDtoList.add(weatherDto);
            } catch (Exception e) {
                throw new RuntimeException("!Ошибка в преобразовании json"); // TODO
            }
        }
        return weatherDtoList;
    }

    private <T> T parseJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("!Ошибка в преобразовании json"); // TODO : need create custom exc
        }
    }

    private <T> T parseJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("!Ошибка в преобразовании json (typeReference)"); // TODO : need create custom exc
        }
    }

}
