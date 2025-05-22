package com.runaumov.spring.service;

import com.runaumov.spring.api.WeatherApiClient;
import com.runaumov.spring.config.TestHibernateConfig;
import com.runaumov.spring.config.TestServiceConfig;
import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dao.UserSessionDao;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.entity.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestServiceConfig.class, TestHibernateConfig.class})
@Transactional
public class UserSessionCleanupServiceIntegrationTest {

    @Autowired
    private UserSessionDao userSessionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserSessionCleanupService userSessionCleanupService;

    @Captor
    private ArgumentCaptor<LocalDateTime> dateTimeArgumentCaptor;

    @MockitoBean
    private WeatherApiClient weatherApiClient;

    private LocalDateTime now;
    private User testUser;

    @BeforeEach
    public void setup() {
        now = LocalDateTime.now();

        testUser = new User();
        testUser.setLogin("testLogin");
        testUser.setPassword("testPassword");
        userDao.save(testUser);
    }

    @Test
    public void shouldRemoveSession() {
        UserSession expiredSession = new UserSession(
                UUID.randomUUID(),
                testUser,
                now.minusHours(2)
        );

        UserSession validSession = new UserSession(
                UUID.randomUUID(),
                testUser,
                now.plusHours(2)
        );

        userSessionDao.save(expiredSession);
        userSessionDao.save(validSession);

        userSessionCleanupService.removeExpiredSessions();

        Optional<UserSession> expiredUserSessionOptional = userSessionDao.findById(expiredSession.getId());
        assertTrue(expiredUserSessionOptional.isEmpty());

        Optional<UserSession> validUserSessionOptional = userSessionDao.findById(validSession.getId());
        assertTrue(validUserSessionOptional.isPresent());
        assertEquals(validSession.getId(), validUserSessionOptional.get().getId());
    }
}
