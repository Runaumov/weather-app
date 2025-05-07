package com.runaumov.spring.interceptor;

import com.runaumov.spring.dto.UserSessionDto;
import com.runaumov.spring.service.UserSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.UUID;

@Component
public class LoginPageBlockInterceptor implements HandlerInterceptor {

    private final UserSessionService userSessionService;

    @Autowired
    public LoginPageBlockInterceptor(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_TOKEN".equals(cookie.getName())) {
                    UUID sessionId = UUID.fromString(cookie.getValue());
                    userSessionService.getValidatedUserSessionDto(sessionId);
                    return true;
                }
            }
        }
        return true;
    }

    private void clearSessionCookie(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("SESSION_TOKEN", null);
        expiredCookie.setMaxAge(0);
        expiredCookie.setPath("/");
        response.addCookie(expiredCookie);
    }
}
