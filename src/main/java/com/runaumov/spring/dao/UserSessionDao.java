package com.runaumov.spring.dao;

import com.runaumov.spring.entity.User;
import com.runaumov.spring.entity.UserSession;
import lombok.Cleanup;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserSessionDao implements iDao<UserSession, UUID> {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserSessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Optional<UserSession> findById(UUID uuid) {
        try (Session session = sessionFactory.openSession()) {
            UserSession userSession = session.createQuery("SELECT us FROM UserSession us " +
                                    "LEFT JOIN FETCH us.user WHERE us.id = :uuid", UserSession.class)
                    .setParameter("uuid", uuid)
                    .uniqueResult();
            return Optional.ofNullable(userSession);
        }
    }

    @Override
    public UserSession create(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(userSession);
            session.getTransaction().commit();
            return userSession;
        }
    }

    @Override
    public Optional<UserSession> update(UserSession model) {
        return Optional.empty();
    }

    @Override
    public void delete(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(userSession);
            session.getTransaction().commit();
        }
    }

    public void deleteById(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        UserSession userSession = session.get(UserSession.class, uuid);
        if (userSession != null) {
            session.remove(userSession);
        }
    }

    public Optional<Integer> getUserIdBySessionId(UUID uuid) {
        try (Session session = sessionFactory.openSession()) {
            int userId = session.createQuery("SELECT us FROM UserSession us WHERE us.id = :uuid ORDER BY us.expiresAt DESC", UserSession.class)
                    .setParameter("uuid", uuid)
                    .setMaxResults(1)
                    .uniqueResult()
                    .getUser()
                    .getId();
            return Optional.of(userId);
        }
    }
}
