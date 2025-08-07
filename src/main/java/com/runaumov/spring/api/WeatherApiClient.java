package com.runaumov.spring.api;

import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.exception.WeatherApiRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class WeatherApiClient {

    private static final String GEOCODING_URL = "http://api.openweathermap.org/geo/1.0/direct";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String NUMBER_OF_CITIES = "5";
    private static final String EXCLUDE_PARAMETRES = "minutely,hourly,alerts";
    private static final String UNIT_OF_MEASUREMENT = "metric";
    @Value("${weather.api.key}") private String apiKey;

    HttpClient client = HttpClient.newHttpClient();

    public String getCityJson(String cityName) {
        try {
            String url = String.format("%s?q=%s&limit=%s&apiKey=%s", GEOCODING_URL, cityName, NUMBER_OF_CITIES, apiKey);
            HttpResponse<String> response = client.send(getRequest(url), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 ) {
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
                    WEATHER_URL, locationDto.getLatitude(), locationDto.getLongitude(), EXCLUDE_PARAMETRES, UNIT_OF_MEASUREMENT, apiKey);
            HttpResponse<String> response = client.send(getRequest(url), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 ) {
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
