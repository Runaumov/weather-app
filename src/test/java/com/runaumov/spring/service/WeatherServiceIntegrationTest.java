package com.runaumov.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runaumov.spring.api.WeatherApiClient;
import com.runaumov.spring.config.TestHibernateConfig;
import com.runaumov.spring.config.TestServiceConfig;
import com.runaumov.spring.dto.CityDto;
import com.runaumov.spring.dto.CityNameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestServiceConfig.class, TestHibernateConfig.class})
@WebAppConfiguration
public class WeatherServiceIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WeatherApiClient weatherApiClient;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(objectMapper, weatherApiClient);
    }

    @Test
    public void shouldReturnCitiesList_WhenCityNameIsValid() throws Exception {
        String citiesJson = "[{\"name\":\"Moscow\",\"country\":\"Russia\",\"lat\":55.7558,\"lon\":37.6173}]";
        CityNameRequest request = new CityNameRequest("Moscow");

        when(weatherApiClient.getCityJson("Moscow")).thenReturn(citiesJson);
        List<CityDto> result = weatherService.getCitiesList(request);

        assertEquals(1, result.size());
        CityDto city = result.get(0);
        assertEquals("Moscow", city.getCityName());
        assertEquals("Russia", city.getCountry());
        assertEquals("55.7558", city.getLatitude());
        assertEquals("37.6173", city.getLongitude());
    }
}
