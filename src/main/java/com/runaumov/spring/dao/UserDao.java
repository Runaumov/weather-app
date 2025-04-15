package com.runaumov.spring.dao;

import com.runaumov.spring.entity.User;
import lombok.Cleanup;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao implements iDao<User, String> {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User create(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user;
        }
    }

    public Optional<User> findByUsername(String login) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
            return Optional.ofNullable(user);
        }
    }

    public User getReferenceById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.getReference(User.class, id);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User model) {
        return Optional.empty();
    }

    @Override
    public void delete(User model) {

    }
}
