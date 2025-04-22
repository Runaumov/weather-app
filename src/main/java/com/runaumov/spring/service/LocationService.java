package com.runaumov.spring.service;

import com.runaumov.spring.dao.LocationDao;
import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                userDao.findById(locationDto.getUserId()).orElseThrow(RuntimeException::new), // TODO
                locationDto.getLatitude(),
                locationDto.getLongitude());
        locationDao.save(location);
        System.out.println(location.toString());
    }
}
