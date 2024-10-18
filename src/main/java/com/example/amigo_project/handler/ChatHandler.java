package com.example.amigo_project.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatHandler extends TextWebSocketHandler {


    // 메시지 처리하는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        // session은 PK 값이고, message에 모든 메시지가 담김
        String msg = message.getPayload();
        String send = msg + " 서버에서 보냄";
        session.sendMessage(new TextMessage(send));
        super.handleTextMessage(session, message);
    }
    
    // 입장 시 처리하는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("유저 들어옴");
        super.afterConnectionEstablished(session);
    }
    
    // 퇴장 시 처리하는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
