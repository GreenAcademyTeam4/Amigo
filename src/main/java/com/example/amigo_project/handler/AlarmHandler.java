package com.example.amigo_project.handler;

import com.example.amigo_project.dto.chat.AlarmDTO;
import com.example.amigo_project.dto.chat.MessageDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.UserService;
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
public class AlarmHandler extends TextWebSocketHandler {

    private final UserService userService;

    // 알람 소켓에 온 유저 관리
    private Map<Integer, WebSocketSession> userManage = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
            AlarmDTO alarmDTO = mapper.readValue(message.getPayload(), AlarmDTO.class);
            for(Integer receiver : userManage.keySet()) {
                if(receiver == alarmDTO.getReceiverId()) {
                    // alarmDTO 안에 담긴 받는이 에게  실시간 메세지 전달
                    User sender = userService.findUser(alarmDTO.getSenderId());
                    String profile = sender.base64Encoding(sender.getProfile());
                    alarmDTO.setSenderNickname(sender.getNickname());
                    alarmDTO.setSenderProfile(profile);
                    String alarm = mapper.writeValueAsString(alarmDTO);
                    userManage.get(receiver).sendMessage(new TextMessage(alarm));
                }
            }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User)session.getAttributes().get("principal");
        // 유저 pk를 키 값으로 넣고 밸류를 웹소켓 세션으로 저장
        userManage.put(user.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User)session.getAttributes().get("principal");
        // 퇴장 할때 유저 pk로 정보 제거
        userManage.remove(user.getId());
    }
}
