package com.example.amigo_project.config;

import com.example.amigo_project.errors.Exception403;
import com.example.amigo_project.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new Exception403("접근이 금지되었습니다.");
        }

        User user = (User) session.getAttribute("user");
        if (user == null || user.getUserRole().equals(0)) {
            throw new Exception403("관리자 권한이 필요합니다.");
        }

        return true;
    }
}
