package com.runaumov.spring.api;

import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.exception.WeatherApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    @Autowired
    public WeatherApiClient(HttpClient client) {
        this.client = client;
    }

    public String getCityJson(String cityName) {
        try {
            String url = String.format("%s?q=%s&limit=%s&apiKey=%s", geocodingUrl, cityName, numberOfCities, apiKey);
            HttpResponse<String> response = client.send(getRequest(url), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new WeatherApiRequestException(String.format("Ошибка при взаимодействии с внешним API, код ответа внешенего сервера:%S", response.statusCode()));
            }
        } catch (Exception e) {
            throw new WeatherApiRequestException("Ошибка при взаимодействии с внешним API");
        }
    }

    public String getWeatherJson(LocationDto locationDto) {
        try {
            String url = String.format("%s?lat=%s&lon=%s&exclude=%s&units=%s&apiKey=%s",
                    weatherUrl, locationDto.getLatitude(), locationDto.getLongitude(), excludeParametres, unitOfMeasurement, apiKey);
            HttpResponse<String> response = client.send(getRequest(url), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new WeatherApiRequestException(String.format("Ошибка при взаимодействии с внешним API, код ответа внешенего сервера:%S", response.statusCode()));
            }

        } catch (Exception e) {
            throw new WeatherApiRequestException("Ошибка при взаимодействии с внешним API");
        }
    }

    private HttpRequest getRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }

}