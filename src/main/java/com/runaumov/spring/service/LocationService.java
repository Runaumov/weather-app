package com.runaumov.spring.service;

import com.runaumov.spring.dao.LocationDao;
import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.entity.Location;
import com.runaumov.spring.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationDao locationDao;
    private final UserDao userDao;

    @Autowired
    public LocationService(LocationDao locationDao, UserDao userDao) {
        this.locationDao = locationDao;
        this.userDao = userDao;
    }

    @Transactional
    public void addLocation(LocationDto locationDto) {
        Location location = new Location(
                locationDto.getName(),
                userDao.findById(locationDto.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(
                                String.format("Пользователь с ID %s не найден на сервере", locationDto.getUserId()))
                        ),
                locationDto.getLatitude(),
                locationDto.getLongitude()
        );

        locationDao.save(location);
    }

    @Transactional
    public List<LocationDto> getLocationDto(Long userId) {
        List<Location> locationList = locationDao.getLocationListByUserId(userId);
        return locationList.stream()
                .map(location -> new LocationDto(
                        location.getId(),
                        location.getUser().getId(),
                        location.getName(),
                        location.getLatitude(),
                        location.getLongitude()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long locationId) {
        locationDao.deleteById(locationId);
    }
}
