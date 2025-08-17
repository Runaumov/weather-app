package com.runaumov.spring.dao.utils;

import com.runaumov.spring.dto.UserSessionDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void setSessionCookie(HttpServletResponse response, UserSessionDto userSessionDto) {
        String sessionToken = userSessionDto.getSessionId().toString();
        String username = userSessionDto.getUserLogin();

        Cookie sessionCookie = new Cookie("SESSION_TOKEN", sessionToken);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true);
        sessionCookie.setPath("/");

        Cookie usernameCookie = new Cookie("username", username);
        usernameCookie.setSecure(true);
        usernameCookie.setPath("/");

        response.addCookie(sessionCookie);
        response.addCookie(usernameCookie);
    }

    public static void deleteCookie(HttpServletResponse response) {
        Cookie expiredSession = new Cookie("SESSION_TOKEN", null);
        expiredSession.setMaxAge(0);
        expiredSession.setPath("/");
        response.addCookie(expiredSession);

        Cookie expiredUsername = new Cookie("username", null);
        expiredUsername.setMaxAge(0);
        expiredUsername.setPath("/");
        response.addCookie(expiredUsername);
    }
}
