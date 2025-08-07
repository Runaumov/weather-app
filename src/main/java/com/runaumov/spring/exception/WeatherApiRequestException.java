package com.runaumov.spring.exception;

public class WeatherApiRequestException extends RuntimeException {

    public WeatherApiRequestException(String message) {
        super(message);
    }
}
