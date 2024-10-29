package com.example.amigo_project.handler;

import com.example.amigo_project.dto.ChatRoomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatHandler extends TextWebSocketHandler {

    // 방 넘버 번호
    Map<WebSocketSession, ChatRoomDTO> school = new ConcurrentHashMap<>();
    

    // 메시지 처리하는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        school.get(session);
    for(WebSocketSession s : school.keySet()){
        if(s != session){
            if(school.get(s).getSchool().equals(school.get(session).getSchool())
              &&school.get(s).getGrade() == school.get(session).getGrade()
              &&school.get(s).getClassroom() == school.get(session).getClassroom()){
                System.out.println(message.getPayload());
                s.sendMessage(new TextMessage(message.getPayload()));
            }
        }

    }


    }

    // 입장 시 처리하는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ChatRoomDTO chatRoomDTO = (ChatRoomDTO) session.getAttributes().get("chatRoomDTO");
        school.put(session, chatRoomDTO);

        System.out.println(session);
        System.out.println("정보 저장됨 : " + chatRoomDTO);
        super.afterConnectionEstablished(session);
    }
    
    // 퇴장 시 처리하는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
