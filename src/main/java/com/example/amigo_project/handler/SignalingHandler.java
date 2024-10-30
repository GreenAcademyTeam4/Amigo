package com.example.amigo_project.handler;

import com.example.amigo_project.dto.chat.MessageDTO;
import com.example.amigo_project.repository.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignalingHandler extends TextWebSocketHandler {

    // 상대방 id를 key로 설계
    private final Map<Integer, WebSocketSession> userManage = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = (User)session.getAttributes().get("principal");
        MessageDTO messageDTO = mapper.readValue(message.getPayload(), MessageDTO.class);
        if(messageDTO.getType().equals("roomId")) {
            // 처음 입장할때 상대방의 id로 내 세션을 저장
            int roomId = Integer.parseInt(messageDTO.getMessage());
            userManage.put(roomId, session);
        } else if(messageDTO.getType().equals("out")) {
            // 채팅을 나갈때 상대방 id로 저장해놓은 내 세션을 지움
            int roomId = Integer.parseInt(messageDTO.getMessage());
            userManage.remove(roomId);
        } else {
            // ice 후보와 sdp 교환 요청이면 내 id로 상대방의 세션을 찾아서 sdp와 ice 후보들을 전송
            for(Integer userId : userManage.keySet()) {
                if(userId == user.getId()) {
                    userManage.get(userId).sendMessage(new TextMessage(message.getPayload()));
                }
            }
        }
    }

}
