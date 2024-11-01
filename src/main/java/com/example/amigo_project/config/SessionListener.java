package com.example.amigo_project.config;

import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
@RequiredArgsConstructor
public class SessionListener implements HttpSessionListener {

    private final UserService userService;
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // 유저 세션이 끊기면 유저를 오프라인으로 전환
        User user = (User)se.getSession().getAttribute("principal");
        userService.updateOffline(user.getId());
    }

}
