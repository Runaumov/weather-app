package com.runaumov.spring.dao;

import com.runaumov.spring.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
        return user;
    }

    public Optional<User> findByUsername(String login) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", login)
                .uniqueResult();
        return Optional.ofNullable(user);
    }

    public User getReferenceById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.getReference(User.class, id);
    }

    public Optional<User> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .uniqueResult();
        return Optional.ofNullable(user);
    }

}
