package com.runaumov.spring.dao;

import com.runaumov.spring.entity.UserSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserSessionDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserSessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Optional<UserSession> findById(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        UserSession userSession = session.createQuery("SELECT us FROM UserSession us " +
                        "LEFT JOIN FETCH us.user WHERE us.id = :uuid", UserSession.class)
                .setParameter("uuid", uuid)
                .uniqueResult();
        return Optional.ofNullable(userSession);
    }

    public UserSession save(UserSession userSession) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(userSession);
        return userSession;
    }

    public void delete(UserSession userSession) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(userSession);
    }

    public void deleteById(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        session.createMutationQuery("DELETE FROM UserSession us WHERE us.id = :uuid")
                .setParameter("uuid", uuid)
                .executeUpdate();
    }

    public void deleteExpiredUserSessions(LocalDateTime now) {
        Session session = sessionFactory.getCurrentSession();
        session.createMutationQuery("DELETE FROM UserSession us WHERE us.expiresAt < :now")
                .setParameter("now", now)
                .executeUpdate();
    }

    public Optional<Long> findUserIdBySessionId(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        UserSession userSession = session.createQuery("SELECT us FROM UserSession us WHERE us.id = :uuid ORDER BY us.expiresAt DESC", UserSession.class)
                .setParameter("uuid", uuid)
                .setMaxResults(1)
                .uniqueResult();

        return Optional.ofNullable(userSession).map(us -> us.getUser().getId());
    }
}
