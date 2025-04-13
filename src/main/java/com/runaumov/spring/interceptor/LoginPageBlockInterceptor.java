package com.runaumov.spring.interceptor;

import com.runaumov.spring.dto.UserSessionDto;
import com.runaumov.spring.service.SessionManagerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class LoginPageBlockInterceptor implements HandlerInterceptor {

    private final SessionManagerService sessionManagerService;

    public LoginPageBlockInterceptor(SessionManagerService sessionManagerService) {
        this.sessionManagerService = sessionManagerService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_TOKEN".equals(cookie.getName())) {
                    try {
                        UUID sessionId = UUID.fromString(cookie.getValue());
                        UserSessionDto userSessionDto = sessionManagerService.getValidatedUserSessionDto(sessionId);

                        response.sendRedirect(request.getContextPath() + "/");
                        return false;
                    } catch (Exception ignored) {
                        // TODO
                    }
                }
            }
        }
        return true;
    }
}
