package com.runaumov.spring.service;

import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dao.UserSessionDao;
import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserSessionDto;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.entity.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionDao userSessionDao;
    @Autowired
    private UserDao userDao;

    private static final int SESSION_LIFETIME = 30;

    public UserSessionDto createNewUserSession(UserAuthenticatedDto userAuthenticatedDto) {
        UserSession userSession = new UserSession();

        User userProxy = userDao.getReferenceById(userAuthenticatedDto.getId());
        //User userProxy = userDao.findByUsername(authenticatedUserDto.getLogin()).get();

        userSession.setId(generateSessionToken());
        userSession.setUser(userProxy);
        userSession.setExpiresAt(generateExpiresAt());
        UserSession createdUserSession = userSessionDao.create(userSession);

        return UserSessionDto.builder()
                .sessionId(createdUserSession.getId())
                .userId(userAuthenticatedDto.getId())
                .userLogin(userAuthenticatedDto.getLogin())
                .expiresAt(createdUserSession.getExpiresAt())
                .build();
    }

    public UserSessionDto getValidatedUserSessionDto (UUID sessionId) {
        Optional<UserSession> userSession = userSessionDao.findById(sessionId);

        if (!userSession.isPresent()) {
            throw new RuntimeException("@1"); // TODO : удалить куки и сделать редирект на логи (мягкая очистка сессии)
        }

        LocalDateTime timeNow = LocalDateTime.now();
        boolean isExpired = userSession.get().getExpiresAt().isBefore(timeNow);

        if (isExpired) {
            userSessionDao.delete(userSession.get());
            throw new RuntimeException("@2"); // TODO
        }

        return UserSessionDto.builder()
                .sessionId(userSession.get().getId())
                .userId(userSession.get().getUser().getId())
                .userLogin(userSession.get().getUser().getLogin())
                .expiresAt(userSession.get().getExpiresAt())
                .build();

    }

    public boolean isValidSession(UUID userSessionId) {
        Optional<UserSession> userSession = userSessionDao.findById(userSessionId);

        if (!userSession.isPresent()) {
            return false;
        }

        LocalDateTime timeNow = LocalDateTime.now();
        boolean isExpired = userSession.get().getExpiresAt().isBefore(timeNow);

        if (isExpired) {
            userSessionDao.delete(userSession.get());
        }

        return !isExpired;
    }

    public UserSession getUserSessionById(UUID uuid) {
        // TODO : Обработать исключение
        return userSessionDao.findById(uuid).orElseThrow(RuntimeException::new);
    }

    public void removeSession(UUID uuid) {
        userSessionDao.deleteById(uuid);
    }

    private UUID generateSessionToken() {
        return UUID.randomUUID();
    }

    private LocalDateTime generateExpiresAt() {
        return LocalDateTime.now().plusMinutes(SESSION_LIFETIME);
    }

}
