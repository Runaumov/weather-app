package com.runaumov.spring.service;

import com.runaumov.spring.api.WeatherApiClient;
import com.runaumov.spring.config.TestHibernateConfig;
import com.runaumov.spring.config.TestServiceConfig;
import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dao.UserSessionDao;
import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserSessionDto;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.entity.UserSession;
import com.runaumov.spring.exception.SessionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestServiceConfig.class, TestHibernateConfig.class})
@Transactional
public class UserSessionServiceIntegrationTest {

    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private UserSessionDao userSessionDao;
    @Autowired
    private UserDao userDao;

    private User testUser;

    private UserAuthenticatedDto userAuthenticatedDto;

    @MockitoBean
    private WeatherApiClient weatherApiClient; // TODO : если в дальнейших тестах нужен будет weatherApiClient,
    // то этот мок можно будет оставить. Если нет, то нужно будет что-то придумать
    // (например, исключить этот бин из конфига). Самый лучший подход - сделать свои конфиги для каждого набора тестов

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setLogin("testLogin");
        testUser.setPassword("testPassword");
        userDao.save(testUser);

        userAuthenticatedDto = new UserAuthenticatedDto(testUser.getId(), testUser.getLogin());
    }

    @Test
    public void shouldCreateNewUserSession_whenUserExist() {

        UserSessionDto result = userSessionService.createNewUserSession(userAuthenticatedDto);

        assertNotNull(result);
        assertNotNull(result.getSessionId());

        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(testUser.getLogin(), result.getUserLogin());

        Optional<UserSession> savedSession = userSessionDao.findById(result.getSessionId());
        assertTrue(savedSession.isPresent());
        assertEquals(testUser.getId(), savedSession.get().getUser().getId());

    }

    @Test
    public void shouldGetValidatedUserSessionDto_whenSessionExist() {
        UserSessionDto createdSession = userSessionService.createNewUserSession(userAuthenticatedDto);
        UUID sessionId = createdSession.getSessionId();

        UserSessionDto result = userSessionService.getValidatedUserSessionDto(sessionId);

        assertNotNull(result);
        assertEquals(sessionId, result.getSessionId());
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(testUser.getLogin(), result.getUserLogin());
    }

    @Test
    public void shouldThrowException_whenSessionNotFound() {
        UUID nonExistentSessionId = UUID.randomUUID();

        assertThrows(SessionNotFoundException.class, () ->
                userSessionService.getValidatedUserSessionDto(nonExistentSessionId));
    }

    @Test
    public void shouldThrowException_andDeleteSession_whenSessionExpired() {
        UserSessionDto createdSession = userSessionService.createNewUserSession(userAuthenticatedDto);
        UUID sessionId = createdSession.getSessionId();

        Optional<UserSession> userSessionOptional = userSessionDao.findById(sessionId);
        assertTrue(userSessionOptional.isPresent());
        UserSession userSession = userSessionOptional.get();
        userSession.setExpiresAt(LocalDateTime.now().minusMinutes(10));
        userSessionDao.save(userSession);

        assertThrows(SessionNotFoundException.class, () ->
                userSessionService.getValidatedUserSessionDto(sessionId));

        assertFalse(userSessionDao.findById(sessionId).isPresent());
    }

    @Test
    public void shouldGetUserId_whenSessionExist() {
        UserSessionDto createdSession = userSessionService.createNewUserSession(userAuthenticatedDto);
        UUID sessionId = createdSession.getSessionId();

        int userId = userSessionService.getUserIdByUserSessionId(sessionId);

        assertEquals(testUser.getId(), userId);
    }

    @Test
    public void shouldThrowException_whenGetUserIdWithNonExistSession() {
        UUID existSessionId = UUID.randomUUID();

        assertThrows(SessionNotFoundException.class, () ->
                userSessionService.getUserIdByUserSessionId(existSessionId));
    }

    @Test
    public void shouldRemoveSession_whenExist() {
        UserSessionDto createdSession = userSessionService.createNewUserSession(userAuthenticatedDto);
        UUID sessionId = createdSession.getSessionId();
        assertTrue(userSessionDao.findById(sessionId).isPresent());

        userSessionService.removeSession(sessionId);

        assertFalse(userSessionDao.findById(sessionId).isPresent());
    }

    @Test
    public void shouldNotThrowException_whenRemovingNonExistSession() {
        UUID nonExistSessionId = UUID.randomUUID();
        assertDoesNotThrow(() -> userSessionService.removeSession(nonExistSessionId));
    }

}