package com.example.amigo_project.config;

import com.example.amigo_project.errors.Exception403;
import com.example.amigo_project.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("AdminInterceptor preHandle 실행");
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new Exception403("접근이 금지되었습니다.");
        }

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null || sessionUser.getUserRole().equals("ADMIN")) {
            //if (sessionUser == null || ! sessionUser.isAdmin()) { // isAdmin() 메서드는 User 클래스에 구현되어 있다고 가정
            throw new Exception403("관리자 권한이 필요합니다.");
        }

        // 관리자 권한이 있는 경우 계속 진행
        return true;
    }

}

