package com.runaumov.spring.service;

import com.runaumov.spring.dao.UserSessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserSessionCleanupService {

    private static final long CLEANUP_RATE_MS = 30 * 60 * 1000;

    private final UserSessionDao userSessionDao;

    @Autowired
    public UserSessionCleanupService(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    @Scheduled(fixedRate = CLEANUP_RATE_MS)
    @Transactional
    public void removeExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        userSessionDao.deleteExpiredUserSessions(now);
    }
}
