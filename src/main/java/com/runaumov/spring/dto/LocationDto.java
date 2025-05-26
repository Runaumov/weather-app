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
public class LocationDto {
    private Long locationId;
    private Long userId;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public LocationDto(Long userId, String name, BigDecimal latitude, BigDecimal longitude) {
        this.userId = userId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}