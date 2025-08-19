package com.runaumov.spring.service;

import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserDto;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.exception.AuthenticationFailedException;
import com.runaumov.spring.exception.RegistrationFailedException;
import com.runaumov.spring.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public UserAuthenticatedDto getAuthenticatedUserDto(UserDto userDto) {
        String login = userDto.getLogin();
        String password = userDto.getPassword();
        Optional<User> optionalUser = userDao.findByUsername(login);

        return optionalUser.filter(user -> PasswordUtil.verifyPassword(password, user.getPassword()))
                .map(user -> new UserAuthenticatedDto(user.getId(), user.getLogin()))
                .orElseThrow(() -> new AuthenticationFailedException("Неверный логин или пароль"));
    }

    @Transactional
    public UserAuthenticatedDto registerNewUser(UserDto userDto) {
        Optional<User> existingUser = userDao.findByUsername(userDto.getLogin());
        if (existingUser.isPresent()) {
            throw new RegistrationFailedException("Пользователь уже существует и не может быть заново зарегестрирован!");
        }

        User userToSave = User.builder()
                .login(userDto.getLogin())
                .password(PasswordUtil.hashPassword(userDto.getPassword()))
                .build();

        User savedUser = userDao.save(userToSave);
        return new UserAuthenticatedDto(savedUser.getId(), savedUser.getLogin());
    }

}
