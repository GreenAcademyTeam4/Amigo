package com.example.amigo_project.config;

import com.example.amigo_project.dto.ChatRoomDTO;
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
public class WebHandShakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
        HttpSession session = req.getSession(false);

        if (session != null) {
            ChatRoomDTO chatRoomDTO = (ChatRoomDTO) session.getAttribute("chatRoomDTO");
            if (chatRoomDTO != null) {
                attributes.put("chatRoomDTO", chatRoomDTO);  // 세션에서 가져온 chatRoomDTO를 WebSocket attributes에 저장
            } else {
                System.out.println("chatRoomDTO가 세션에 없습니다.");
            }
        } else {
            System.out.println("세션이 없습니다.");
        }

        return true;  // 핸드셰이크가 성공하도록 true를 반환
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 핸드셰이크 이후 처리할 로직이 필요할 경우 여기에 추가
    }
}
