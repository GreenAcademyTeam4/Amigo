package com.example.amigo_project.config;

import com.example.amigo_project.dto.chat.RoomDataDTO;
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
//HandshakeInterceptor : 웹소켓 연결 전에 사용자 세션 확인
public class ChatInterceptor implements HandshakeInterceptor {


    // 웹소켓 연결 전에 낚아챔
    // WebSocket 연결이 시작되기 전에 사용자 정보를 세션에서 가져와 WebSocket 세션의 속성에 저장
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();
        HttpSession session = req.getSession(false); // 세션이 없으면 null 반환
        if (session != null) {
            User user = (User)session.getAttribute("principal");

        }
        return true;
    }

    // 웹소켓 연결 후에 낚아챔
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
