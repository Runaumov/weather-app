package com.runaumov.spring.dao;

import com.runaumov.spring.entity.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Location location) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(location);
    }

    public List<Location> getLocationListByUserId(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT l FROM Location l WHERE l.user.id = :id", Location.class)
                .setParameter("id", id)
                .getResultList();
    }

}
