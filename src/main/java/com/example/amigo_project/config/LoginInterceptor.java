package com.example.amigo_project.config;

import com.example.amigo_project.errors.Exception401;
import com.example.amigo_project.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        if(session == null) {
            throw new Exception401("로그인이 필요 합니다");
        }

        User user = (User) session.getAttribute("user");
        if(user == null) {
            throw new Exception401("로그인이 필요 합니다");
        }

        return true;
    }
}
