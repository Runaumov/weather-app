package com.runaumov.spring.dao;

import com.runaumov.spring.dto.LocationDto;
import com.runaumov.spring.entity.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Location location) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(location);
    }

}
