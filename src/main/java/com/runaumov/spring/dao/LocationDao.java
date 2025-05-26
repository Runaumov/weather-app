package com.runaumov.spring.dao;

import com.runaumov.spring.entity.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LocationDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public LocationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Location location) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(location);
    }

    public List<Location> getLocationListByUserId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT l FROM Location l WHERE l.user.id = :id", Location.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Optional<Location> getLocationByLocationId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Location location = session.createQuery("SELECT l FROM Location l WHERE l.id = :id", Location.class)
                .setParameter("id", id)
                .uniqueResult();
        return Optional.ofNullable(location);
    }

    public void deleteById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        session.createMutationQuery("DELETE FROM Location l WHERE l.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

}
