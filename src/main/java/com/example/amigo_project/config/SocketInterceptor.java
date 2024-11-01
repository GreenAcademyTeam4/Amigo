package com.example.amigo_project.config;

import com.example.amigo_project.dto.chat.RoomDataDTO;
import com.example.amigo_project.errors.Exception401;
import com.example.amigo_project.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class SocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();
        HttpSession session = req.getSession(false); // 세션이 없으면 null 반환
        if (session != null) {
            RoomDataDTO data = (RoomDataDTO) session.getAttribute("roomData");
            User user = (User)session.getAttribute("principal");
            if (data != null) {
                attributes.put("roomData", data); // WebSocketSession에 저장할 데이터 추가
                attributes.put("principal", user);
            } else {
                attributes.put("principal", user);
            }
        } else {
            throw new Exception401("로그인이 필요합니다");
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}


