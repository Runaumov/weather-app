package com.runaumov.spring.service;

import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dao.UserSessionDao;
import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserSessionDto;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.entity.UserSession;
import com.runaumov.spring.exception.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSessionService {

    private final UserSessionDao userSessionDao;
    private final UserDao userDao;

    private static final int SESSION_LIFETIME = 30;

    @Autowired
    public UserSessionService(UserDao userDao, UserSessionDao userSessionDao) {
        this.userDao = userDao;
        this.userSessionDao = userSessionDao;
    }

    @Transactional
    public UserSessionDto createNewUserSession(UserAuthenticatedDto userAuthenticatedDto) {
        UserSession userSession = new UserSession();

        User userProxy = userDao.getReferenceById(userAuthenticatedDto.getId());

        userSession.setId(UUID.randomUUID());
        userSession.setUser(userProxy);
        userSession.setExpiresAt(generateExpiresAt());
        UserSession createdUserSession = userSessionDao.save(userSession);

        return UserSessionDto.builder()
                .sessionId(createdUserSession.getId())
                .userId(userAuthenticatedDto.getId())
                .userLogin(userAuthenticatedDto.getLogin())
                .expiresAt(createdUserSession.getExpiresAt())
                .build();
    }

    @Transactional
    public UserSessionDto getValidatedUserSessionDto (UUID sessionId) {
        UserSession userSession = userSessionDao.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found or expired"));// TODO : удалить куки и сделать редирект на логи (мягкая очистка сессии)

        LocalDateTime timeNow = LocalDateTime.now();
        boolean isExpired = userSession.getExpiresAt().isBefore(timeNow);

        if (isExpired) {
            handleExpiredSession(userSession);
        }

        return UserSessionDto.builder()
                .sessionId(userSession.getId())
                .userId(userSession.getUser().getId())
                .userLogin(userSession.getUser().getLogin())
                .expiresAt(userSession.getExpiresAt())
                .build();
    }

    @Transactional
    public int getUserIdByUserSessionId(UUID uuid) { //TODO : check calling this method after validation session
        return userSessionDao.findUserIdBySessionId(uuid)
                .orElseThrow(() -> new RuntimeException("1231234"));
    }

    @Transactional
    public void removeSession(UUID uuid) {
        userSessionDao.deleteById(uuid);
    }

    @Transactional
    protected void handleExpiredSession(UserSession userSession) {
        userSessionDao.delete(userSession);
        throw new RuntimeException("@2"); // TODO
    }

    private LocalDateTime generateExpiresAt() {
        return LocalDateTime.now().plusMinutes(SESSION_LIFETIME);
    }

}
