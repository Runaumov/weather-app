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
public class UserLocationDto {
    private int userId;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public UserLocationDto(int userId, BigDecimal longitude, BigDecimal latitude) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
