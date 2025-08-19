package com.runaumov.spring.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runaumov.spring.dto.CityDto;
import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.dto.WeatherDto;
import com.runaumov.spring.exception.WeatherApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherApiClient(HttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
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

            HttpResponse<String> response = client.send(getRequest(uri), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<CityDto>>() {
                });
            } else {
                throw new WeatherApiRequestException(String.format("Ошибка при взаимодействии с внешним API, код ответа внешенего сервера:%S", response.statusCode()));
            }
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

            HttpResponse<String> response = client.send(getRequest(uri), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                WeatherDto weatherDto = objectMapper.readValue(response.body(), WeatherDto.class);
                weatherDto.setCity(locationDto.getName());
                weatherDto.setLocationId(locationDto.getLocationId());
                return weatherDto;
            } else {
                throw new WeatherApiRequestException(String.format("Ошибка при взаимодействии с внешним API, код ответа внешенего сервера:%S", response.statusCode()));
            }

        } catch (Exception e) {
            throw new WeatherApiRequestException("Ошибка при взаимодействии с внешним API");
        }
    }

    private HttpRequest getRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
    }

}