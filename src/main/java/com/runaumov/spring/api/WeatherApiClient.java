package com.runaumov.spring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runaumov.spring.dto.CityDto;
import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.dto.WeatherDto;
import com.runaumov.spring.exception.WeatherApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class WeatherApiClient {

    @Value("${weather.api.geocoding.url}")
    private String geocodingUrl;
    @Value("${weather.api.current.url}")
    private String weatherUrl;
    @Value("${weather.api.geocoding.limit}")
    private int numberOfCities;
    @Value("${weather.api.exclude.parameters}")
    private String excludeParametres;
    @Value("${weather.api.units}")
    private String unitOfMeasurement;
    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherApiClient(RestTemplate restTemplate, HttpClient client, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
    }

    public List<CityDto> getCitiesList(String cityName) {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(geocodingUrl)
                    .queryParam("q", cityName)
                    .queryParam("limit", numberOfCities)
                    .queryParam("apiKey", apiKey)
                    .encode()
                    .build()
                    .toUri();

            CityDto[] cityDtoList = restTemplate.getForObject(uri, CityDto[].class);
            return cityDtoList != null ? Arrays.asList(cityDtoList) : Collections.emptyList();

        } catch (Exception e) {
            throw new WeatherApiRequestException("Ошибка при взаимодействии с внешним API");
        }
    }

    public WeatherDto getWeatherDto(LocationDto locationDto) {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(weatherUrl)
                    .queryParam("lat", locationDto.getLatitude())
                    .queryParam("lon", locationDto.getLongitude())
                    .queryParam("exclude", excludeParametres)
                    .queryParam("units", unitOfMeasurement)
                    .queryParam("apiKey", apiKey)
                    .encode()
                    .build()
                    .toUri();

            WeatherDto weatherDto = restTemplate.getForObject(uri, WeatherDto.class);

            if (weatherDto != null) {
                weatherDto.setCity(locationDto.getName());
                weatherDto.setLocationId(locationDto.getLocationId());
            }

            return weatherDto;

        } catch (Exception e) {
            throw new WeatherApiRequestException("Ошибка при взаимодействии с внешним API");
        }
    }

}