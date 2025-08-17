package com.runaumov.spring.service;

import com.runaumov.spring.api.WeatherApiClient;
import com.runaumov.spring.config.TestHibernateConfig;
import com.runaumov.spring.config.TestServiceConfig;
import com.runaumov.spring.dao.UserDao;
import com.runaumov.spring.dto.UserAuthenticatedDto;
import com.runaumov.spring.dto.UserDto;
import com.runaumov.spring.entity.User;
import com.runaumov.spring.exception.AuthenticationFailedException;
import com.runaumov.spring.exception.RegistrationFailedException;
import com.runaumov.spring.dao.utils.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestServiceConfig.class, TestHibernateConfig.class})
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    private UserDto userDto;

    private User testUser;

    @MockitoBean
    private WeatherApiClient weatherApiClient;

    @BeforeEach
    public void setup() {
        String password = "testPassword";
        String hashPassword = PasswordUtil.hashPassword(password);
        testUser = new User();
        testUser.setLogin("testLogin");
        testUser.setPassword(hashPassword);
        userDao.save(testUser);

        userDto = new UserDto(testUser.getLogin(), password);
    }

    @Test
    public void shouldGetUserAuthenticatedDto_whenCredentialsAreValid() {
        UserAuthenticatedDto result = userService.getAuthenticatedUserDto(userDto);

        assertNotNull(result);

        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getLogin(), result.getLogin());
    }

    @Test
    public void shouldThrowAuthenticationFailedException_whenUserLoginNotFound(){
        UserDto uncorrectUserDto = new UserDto("AnotherLogin", "AnotherPassword");

        assertThrows(AuthenticationFailedException.class, () ->
                userService.getAuthenticatedUserDto(uncorrectUserDto));
    }

    @Test
    public void shouldThrowAuthenticationFailedException_whenPasswordIsInvalid(){
        UserDto uncorrectUserDto = new UserDto("testLogin", "AnotherPassword");

        assertThrows(AuthenticationFailedException.class, () ->
                userService.getAuthenticatedUserDto(uncorrectUserDto));
    }

    @Test
    public void shouldRegisterNewUser_whenLoginNotExists() {
        UserDto registrationUserDto = new UserDto("user", "password");

        UserAuthenticatedDto result = userService.registerNewUser(registrationUserDto);

        assertNotNull(result);

        assertEquals(registrationUserDto.getLogin(), result.getLogin());
    }

    @Test
    public void shouldThrowRegistrationFailedException_whenLoginAlreadyExists() {
        assertThrows(RegistrationFailedException.class, () ->
                userService.registerNewUser(userDto));
    }

    @Test
    public void shouldVerifyPasswordHashingAndVerificationDuringRegistrationAndAuthentication() {
        String newLogin = "newUser";
        String newPassword = "newPassword";

        UserDto registrationUserDto = new UserDto(newLogin, newPassword);
        UserAuthenticatedDto registredUserAuthDto = userService.registerNewUser(registrationUserDto);

        assertNotNull(registredUserAuthDto);
        assertNotNull(registredUserAuthDto.getId());
        assertNotNull(registredUserAuthDto.getLogin());
        assertEquals(registrationUserDto.getLogin(), registredUserAuthDto.getLogin());

        Optional<User> optionalSavedUser = userDao.findByUsername(newLogin);
        assertTrue(optionalSavedUser.isPresent());
        User savedUser = optionalSavedUser.get();

        String savedPassword = savedUser.getPassword();
        assertNotEquals(newPassword, savedPassword);
    }
}
