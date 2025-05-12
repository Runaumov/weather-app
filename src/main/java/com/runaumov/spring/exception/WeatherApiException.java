package com.runaumov.spring.exception;

public class WeatherApiException extends RuntimeException {

    public WeatherApiException(String message) {
        super(message);
    }
}
