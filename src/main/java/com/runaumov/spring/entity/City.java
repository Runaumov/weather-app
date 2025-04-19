package com.runaumov.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class City {

    @JsonProperty("name") private String cityName;
    @JsonProperty("country") private String country;
    @JsonProperty("state") private String state;
    @JsonProperty("lat") private String latitude;
    @JsonProperty("lon") private String longitude;

}
