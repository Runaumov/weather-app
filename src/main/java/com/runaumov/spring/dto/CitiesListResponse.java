package com.runaumov.spring.dto;

import com.runaumov.spring.entity.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CitiesListResponse {
    private List<City> cities;
}
