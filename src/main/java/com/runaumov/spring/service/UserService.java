package com.runaumov.spring.service;

import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserDto;
import com.runaumov.spring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserAuthenticatedDto getAuthenticatedUserDto(UserDto userDto) {
        String login = userDto.getLogin();
        Optional<User> user = userDao.findByUsername(login);
        return user
                .map(u -> new UserAuthenticatedDto(u.getId(), u.getLogin()))
                // TODO : добавить свой обработчик исключений
                .orElseThrow(() -> new RuntimeException("Зашлугка")
                );
    }

    public UserAuthenticatedDto registerNewUser(UserDto userDto) {
        User userToSave = User.builder()
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .build();
        User savedUser = userDao.create(userToSave);

        return new UserAuthenticatedDto(savedUser.getId(), savedUser.getLogin());
    }

    public boolean isUserExist(String login) {
        Optional<User> user = userDao.findByUsername(login);
        return user.isPresent();
    }

}
