package com.example.amigo_project.handler;


import com.example.amigo_project.dto.chat.MessageDTO;
import com.example.amigo_project.repository.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class FriendChatHandler extends TextWebSocketHandler {

    // WebSocketSession: PK 값
    // Integer : 방 번호
    private Map<WebSocketSession, Integer> userManage = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;


    // 메시지 처리하는 기능
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User user = (User)session.getAttributes().get("principal");

        // JSON 형식으로 받은 값을 DTO에 담음
        MessageDTO messageDTO = mapper.readValue(message.getPayload(), MessageDTO.class);
        // 만약 DTO에 담긴 type이 roomKey면
        if(messageDTO.getType().equals("roomKey")){
            // DTO에 담긴 message가 String이어서 int로 형변환
            int roomId = Integer.parseInt(messageDTO.getMessage());
            // 유저 관리하는 곳에 나(session)와 방 번호를 담음
            userManage.put(session, roomId);
            // 만약 DTO에 담김 type이 chat(메시지)이라면
        } else if(messageDTO.getType().equals("chat")){


            // 유저 관리하는 곳에 Key 값인 WebSocketSession 들(나, 유저1, 유저2 등)을 하나씩 반복해서 s에 담음
            for(WebSocketSession s : userManage.keySet()) {
                // 만약 s에 내가 담기고 가져온 session(나) 가 같다면
                if(userManage.get(s) == userManage.get(session)) {
                    // 메시지를 보냄
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
            }
        }


    }

    // 웹소켓과 연결 됐을 때 처리하는 기능
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        super.afterConnectionEstablished(session);
        System.out.println("웹소켓 연결됨~~");
    }

    // 웝소켓과 연결을 끊을 때 처리하는 기능
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        userManage.remove(session);
        super.afterConnectionClosed(session, status);
        System.out.println("웹소켓 연결 끊김~~~");
    }
}

