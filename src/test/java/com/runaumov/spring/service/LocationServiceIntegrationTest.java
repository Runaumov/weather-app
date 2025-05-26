package com.runaumov.spring.service;

import com.runaumov.spring.api.WeatherApiClient;
import com.runaumov.spring.config.HibernateConfig;
import com.runaumov.spring.config.TestHibernateConfig;
import com.runaumov.spring.config.TestServiceConfig;
import com.runaumov.spring.dao.LocationDao;
import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.entity.Location;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestServiceConfig.class, TestHibernateConfig.class})
@Transactional
public class LocationServiceIntegrationTest {

    @Autowired
    private LocationService locationService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LocationDao locationDao;

    @MockitoBean
    private WeatherApiClient weatherApiClient;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setLogin("testLogin");
        testUser.setPassword("testPassword");
        userDao.save(testUser);

        when(weatherApiClient.getWeatherJson(any()))
                .thenReturn("{\"weather\": \"sunny\"}");
    }

    @Test
    public void saveLocation_whenLocationDtoExist() {
        LocationDto locationDto = new LocationDto(testUser.getId(), "Home", BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));

        locationService.addLocation(locationDto);

        List<Location> locations = locationDao.getLocationListByUserId(testUser.getId());
        assertFalse(locations.isEmpty());

        Location savedLocation = locations.get(0);
        assertEquals("Home", savedLocation.getName());
        assertEquals(testUser.getId(), savedLocation.getUser().getId());
        assertEquals(1.0, savedLocation.getLatitude().doubleValue());
        assertEquals(2.0, savedLocation.getLongitude().doubleValue());
    }

    @Test
    public void shouldThrowUserNotFoundException_whenUserNotFound() {
        Random random = new Random();
        Long uncorrectedUserId = random.nextLong(98) + 2;
        LocationDto locationDto = new LocationDto(uncorrectedUserId, "Home", BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));

        assertThrows(UserNotFoundException.class, () ->
                locationService.addLocation(locationDto));
    }

    @Test
    public void shouldGetLocation_whenUserIdExist() {
        LocationDto locationDto1 = new LocationDto(testUser.getId(), "Home", BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));
        LocationDto locationDto2 = new LocationDto(testUser.getId(), "NoHome", BigDecimal.valueOf(3.0), BigDecimal.valueOf(4.0));
        locationService.addLocation(locationDto1);
        locationService.addLocation(locationDto2);
        List<LocationDto> result = locationService.getLocationDto(testUser.getId());

        assertNotNull(result);
        assertEquals(2, result.size());

        LocationDto firstLocationDto = result.get(0);
        assertEquals(testUser.getId(), firstLocationDto.getUserId());
        assertEquals("Home",firstLocationDto.getName());

        LocationDto secondLocationDto = result.get(1);
        assertEquals(testUser.getId(), secondLocationDto.getUserId());
        assertEquals("NoHome",secondLocationDto.getName());
    }

    @Test
    public void shouldReturnEmptyList_whenUserHasNotLocation() {
        List<LocationDto> result = locationService.getLocationDto(testUser.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    public void shouldDeleteLocation_whenLocationIdExist() {
        LocationDto locationDto = new LocationDto(testUser.getId(), "Home", BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));

        locationService.addLocation(locationDto);

        List<Location> locations = locationDao.getLocationListByUserId(testUser.getId());
        assertFalse(locations.isEmpty());

        Long locationId = locations.get(0).getId();

        locationService.deleteById(locationId);
        assertTrue(locationDao.getLocationByLocationId(locationId).isEmpty());
    }

}
