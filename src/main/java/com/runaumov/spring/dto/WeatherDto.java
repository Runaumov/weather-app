package com.runaumov.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherDto {
    private String city;
    private String country;
    private String state;
    private String latitude;
    private String longitude;
    private String temp;
    private String pressureMmHg;
}
