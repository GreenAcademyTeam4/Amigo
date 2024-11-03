package com.example.amigo_project.handler;

import com.example.amigo_project.dto.chat.FriendChatDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.chat.ChatLog;
import com.example.amigo_project.repository.model.chat.Emoticon;
import com.example.amigo_project.service.ChatService;
import com.example.amigo_project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class FriendChatHandler extends TextWebSocketHandler {

    // Map of roomId to Set of WebSocketSessions
    // roomId(방의 고유 번호)를 키로 하고, 해당 방에 속한 WebSocketSession들의 집합(Set)을 값으로 가짐.
    private final Map<Integer, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    // 새로운 맵: 세션과 방 ID를 매핑
    private final Map<WebSocketSession, Integer> sessionRoomMap = new ConcurrentHashMap<>();

    private final UserService userService;
    private final ChatService chatService;
    private final ObjectMapper mapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User user = (User)session.getAttributes().get("principal");
        System.out.println("유저 정보!!! : " + user);
        // JSON 형식 메시지 파싱
        FriendChatDTO messageDTO = mapper.readValue(message.getPayload(), FriendChatDTO.class);

        if ("roomKey".equals(messageDTO.getType())) {
            // roomId 파싱
            int roomId = Integer.parseInt(messageDTO.getMessage().trim()); // 공백 제거 후 파싱
            log.info("Received roomKey: {}", roomId);

            // 로그 불러오기


            // 이전 방에서 세션 제거
            // 사용자가 방을 이동할 때, 이전 방에 더 이상 연결되지 않도록 하기 위함.
            Integer oldRoomId = sessionRoomMap.get(session);
            if (oldRoomId != null && oldRoomId != roomId) {
                Set<WebSocketSession> oldSessions = roomSessions.get(oldRoomId);
                if (oldSessions != null) {
                    oldSessions.remove(session);
                    log.info("Session {} removed from old room {}", session.getId(), oldRoomId);
                }
            }

            // 새로운 방에 세션 추가
            roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
            sessionRoomMap.put(session, roomId);

            log.info("Session {} joined room {}", session.getId(), roomId);
        } else if ("chat".equals(messageDTO.getType())) {
            int roomId = sessionRoomMap.get(session);
            // 현재 날짜를 가져옴
            LocalDate today = LocalDate.now();

            // LocalDate를 LocalDateTime으로 변환
            LocalDateTime startOfDay = today.atStartOfDay();

            // LocalDateTime을 Timestamp로 변환
            Timestamp timestamp = Timestamp.valueOf(startOfDay);

            // 마지막 메시지 날짜와 비교
            LocalDate lastMessageDate = chatService.findLastMessageDate(roomId);
            System.out.println("lastMessageDate: " + lastMessageDate);
            boolean isNewDay = (lastMessageDate == null || !lastMessageDate.equals(today));

            // 메시지를 전송할 때, 날짜가 변경되었는지 확인하여 날짜 정보를 포함시킴
            if (isNewDay) {
                ChatLog date = ChatLog.builder()
                        .userId(user.getId())
                        .createdAt(timestamp)
                        .type("dateLog")
                        .roomId(roomId)
                        .build();
                chatService.createChatLog(date);
                // 날짜 정보 전송 (JSON 형식 예시)
                TextMessage dateMessage = new TextMessage("{\"type\": \"date\", \"date\": \"" + today.toString() + "\"}");
                Set<WebSocketSession> sessions = roomSessions.get(roomId);
                if (sessions != null) {
                    for (WebSocketSession s : sessions) {
                        s.sendMessage(dateMessage);
                    }
                }
                chatService.updateLastMessageDate(roomId, today);
            }
            log.info("Received chat message for roomId: {}", roomId);

            // **채팅 로그 저장 로직 추가**
            // 사용자가 보낸 채팅 메시지를 데이터베이스에 저장
            chatService.saveChatLog(roomId, user.getId(), messageDTO.getType(), messageDTO.getMessage(), messageDTO.getDate());

            // 메시지 전송 (발신자 제외)
            // roomSessions 에서 해당 방(roomId)의 세션 집합을 가져옴.
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            ObjectMapper mapper = new ObjectMapper();
            messageDTO.setSender("receiver");
            String messageJson = mapper.writeValueAsString(messageDTO);
            // 세션이 존재하면, 반복문을 통해 각 세션에 대해 메시지를 전송함.
            int isreceiver = 0;
            if (sessions != null) {
                for (WebSocketSession s : sessions) {
                    if(s == session) {
                        s.sendMessage(new TextMessage(message.getPayload()));
                    } else {
                        s.sendMessage(new TextMessage(messageJson));
                    }
                    isreceiver++;
                }
            }
            // 만약 채팅방에 나 혼자면
            if(isreceiver == 1) {

            }
        } else if("emoticon".equals(messageDTO.getType())){
            int roomId = sessionRoomMap.get(session);
            // 현재 날짜를 가져옴
            LocalDate today = LocalDate.now();

            // LocalDate를 LocalDateTime으로 변환
            LocalDateTime startOfDay = today.atStartOfDay();

            // LocalDateTime을 Timestamp로 변환
            Timestamp timestamp = Timestamp.valueOf(startOfDay);

            // 마지막 메시지 날짜와 비교
            LocalDate lastMessageDate = chatService.findLastMessageDate(roomId);
            System.out.println("lastMessageDate: " + lastMessageDate);
            boolean isNewDay = (lastMessageDate == null || !lastMessageDate.equals(today));

            // 메시지를 전송할 때, 날짜가 변경되었는지 확인하여 날짜 정보를 포함시킴
            if (isNewDay) {
                ChatLog date = ChatLog.builder()
                        .userId(user.getId())
                        .createdAt(timestamp)
                        .type("dateLog")
                        .roomId(roomId)
                        .build();
                chatService.createChatLog(date);
                // 날짜 정보 전송 (JSON 형식 예시)
                TextMessage dateMessage = new TextMessage("{\"type\": \"date\", \"date\": \"" + today.toString() + "\"}");
                Set<WebSocketSession> sessions = roomSessions.get(roomId);
                if (sessions != null) {
                    for (WebSocketSession s : sessions) {
                        s.sendMessage(dateMessage);
                    }
                }

                chatService.updateLastMessageDate(roomId, today);


            }
            log.info("Received chat message for roomId: {}", roomId);

            // **채팅 로그 저장 로직 추가**
            // 사용자가 보낸 채팅 메시지를 데이터베이스에 저장

            chatService.saveChatLog(roomId, user.getId(), messageDTO.getType(), messageDTO.getMessage(), messageDTO.getDate());
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            ObjectMapper mapper = new ObjectMapper();
            messageDTO.setSender("receiver");
            String messageJson = mapper.writeValueAsString(messageDTO);
            // 세션이 존재하면, 반복문을 통해 각 세션에 대해 메시지를 전송함.
            if (sessions != null) {
                for (WebSocketSession s : sessions) {
                    if(s == session) {
                        s.sendMessage(new TextMessage(message.getPayload()));
                    } else {
                        s.sendMessage(new TextMessage(messageJson));
                    }
                }
            }
        }






    }


//    // 웹소켓과 연결 됐을 때 처리하는 기능
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        super.afterConnectionEstablished(session);
//        log.info("웹소켓 연결됨~~");
//
//    }

    // 웹소켓과 연결을 끊을 때 처리하는 기능
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 모든 방에서 세션 제거
        Integer roomId = sessionRoomMap.get(session);
        if (roomId != null) {
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            if (sessions != null) {
                sessions.remove(session);
                log.info("Session {} removed from room {}", session.getId(), roomId);
            }
        }
        // 세션의 방 매핑도 제거
        sessionRoomMap.remove(session);
        super.afterConnectionClosed(session, status);
        log.info("웹소켓 연결 끊김~~~");
    }
}
