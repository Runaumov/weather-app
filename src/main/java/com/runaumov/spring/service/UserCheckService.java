package com.runaumov.spring.service;

import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dto.AuthenticatedUserDto;
import com.runaumov.spring.dto.UserDto;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.entity.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCheckService {

    private final UserDao userDao;

    @Autowired
    public UserCheckService(UserDao userDao) {
        this.userDao = userDao;
    }

    public AuthenticatedUserDto getAuthenticatedUserDto(UserDto userDto) {
        String login = userDto.getLogin();
        Optional<User> user = userDao.findByUsername(login);
        return user
                .map(u -> new AuthenticatedUserDto(u.getId(), u.getLogin()))
                // TODO : добавить свой обработчик исключений
                .orElseThrow(() -> new RuntimeException("Зашлугка")
                );
    }

    private boolean isUserExist(UserDto userDto) {
        String login = userDto.getLogin();
        Optional<User> user = userDao.findByUsername(login);
        return user.isPresent();
    }

}
