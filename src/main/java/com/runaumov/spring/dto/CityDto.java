package com.runaumov.spring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CityDto {

    @JsonProperty("name") private String cityName;
    @JsonProperty("country") private String country;
    @JsonProperty("state") private String state;
    @JsonProperty("lat") private String latitude;
    @JsonProperty("lon") private String longitude;

}
