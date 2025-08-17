package com.runaumov.spring.service;

import com.runaumov.spring.dao.UserSessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserSessionCleanupService {

    private final UserSessionDao userSessionDao;

    @Autowired
    public UserSessionCleanupService(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    @Scheduled(fixedRateString = "${cleanup.rate.ms:1800000}")
    @Transactional
    public void removeExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        userSessionDao.deleteExpiredUserSessions(now);
    }
}
