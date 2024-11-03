package com.example.amigo_project.handler;

import com.example.amigo_project.dto.chat.FriendChatDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.chat.ChatLog;
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

    // roomId(방의 고유 번호)를 키로 하고, 해당 방에 속한 WebSocketSession들의 집합(Set)을 값으로 가짐
    // 각 방(roomId)에 속한 WebSocket 세션을 관리
    // 사용자가 메시지를 보낼 때, 같은 방에 있는 다른 사용자들에게만 메시지를 전송하는 데 사용됨
    private final Map<Integer, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    // 각 WebSocketSession과 방 ID를 매핑하여, 세션이 어떤 방에 속해 있는지 추적
    // 특정 세션이 연결된 방을 추적하여, 세션 종료 시 해당 세션을 방에서 제거할 때 사용
    private final Map<WebSocketSession, Integer> sessionRoomMap = new ConcurrentHashMap<>();

    private final UserService userService;
    private final ChatService chatService;
    private final ObjectMapper mapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User user = (User)session.getAttributes().get("principal");

        // JSON 형식 메시지 파싱
        // message.getPayload()를 통해 JSON 형태의 메시지 내용을 FriendChatDTO 객체로 변환
        FriendChatDTO messageDTO = mapper.readValue(message.getPayload(), FriendChatDTO.class);

        if ("roomKey".equals(messageDTO.getType())) {
            // 사용자가 채팅방에 입장할 때 전달되는 방 ID
            // messageDTO.getMessage()에서 방 ID를 가져와 roomId(int)로 파싱
            int roomId = Integer.parseInt(messageDTO.getMessage().trim()); // 공백 제거 후 파싱
            log.info("Received roomKey: {}", roomId);

            // 기존 방에서 세션 제거
            // 세션이 기존 방에 연결되어 있다면, 먼저 해당 세션을 기존 방에서 제거하고 새 방에 추가함
            // 사용자가 방을 이동할 때, 이전 방에 더 이상 연결되지 않도록 하기 위함
            Integer oldRoomId = sessionRoomMap.get(session);
            if (oldRoomId != null && oldRoomId != roomId) {
                Set<WebSocketSession> oldSessions = roomSessions.get(oldRoomId);
                if (oldSessions != null) {
                    oldSessions.remove(session);
                    log.info("Session {} removed from old room {}", session.getId(), oldRoomId);
                }
            }

            // 새로운 방에 세션 추가
            // roomSessions에 새로운 roomId가 존재하지 않으면 새로운 Set을 추가하고, 현재 세션을 해당 방에 추가함
            roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
            // sessionRoomMap에 현재 세션을 새로운 방 ID로 업데이트
            sessionRoomMap.put(session, roomId);
            log.info("Session {} joined room {}", session.getId(), roomId);

        } else if ("chat".equals(messageDTO.getType())) {
            // sessionRoomMap에서 현재 세션이 속한 방 ID를 가져옴.
            int roomId = sessionRoomMap.get(session);

            // 현재 날짜를 가져옴
            LocalDate today = LocalDate.now();

            // LocalDate를 LocalDateTime으로 변환
            LocalDateTime startOfDay = today.atStartOfDay();

            // LocalDateTime을 Timestamp로 변환
            Timestamp timestamp = Timestamp.valueOf(startOfDay);

            // 마지막 메시지 날짜 조회
            LocalDate lastMessageDate = chatService.findLastMessageDate(roomId);
            
            // 오늘 날짜와 마지막 메시지 날짜와 비교하여 날짜가 변경되었는지 확인
            boolean isNewDay = (lastMessageDate == null || !lastMessageDate.equals(today));

            // 날짜가 변경되었을 때 처리
            // 메시지를 전송할 때, 날짜가 변경되었는지 확인하여 날짜 정보를 포함시킴
            if (isNewDay) {
                ChatLog date = ChatLog.builder()
                        .userId(user.getId())
                        .createdAt(timestamp)
                        .type("dateLog") // 날짜가 변경되면 dateLog 타입의 채팅 로그를 DB에 저장함
                        .roomId(roomId)
                        .build();
                chatService.createChatLog(date);

                // 날짜가 변경되었음을 알리는 메시지를 생성하여, 해당 방에 있는 모든 세션에 전송함
                TextMessage dateMessage = new TextMessage("{\"type\": \"date\", \"date\": \"" + today + "\"}");
                Set<WebSocketSession> sessions = roomSessions.get(roomId);
                if (sessions != null) {
                    for (WebSocketSession s : sessions) {
                        s.sendMessage(dateMessage);
                    }
                }
                chatService.updateLastMessageDate(roomId, today);
            }
            log.info("Received chat message for roomId: {}", roomId);

            // **채팅 로그 저장 로직**
            // 사용자가 보낸 채팅 메시지를 DB에 저장
            chatService.saveChatLog(roomId, user.getId(), messageDTO.getType(), messageDTO.getMessage(), messageDTO.getDate());

            // 메시지 전송 (발신자 제외 나머지 세션들에만 메시지를 전송)
            // 현재 사용자가 속한 방(roomId)에 연결된 모든 세션(WebSocketSession)을 가져옴
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            // messageDTO 객체를 JSON 형식으로 변환하기 위함
            ObjectMapper mapper = new ObjectMapper();
            // 메시지가 다른 사용자에게 전달될 때 발신자가 누구인지 나타내는 데 도움이 됨
            messageDTO.setSender("receiver");
            // messageDTO 객체를 JSON 형식으로 변환
            // 전송할 메시지의 실제 데이터(messageJson)
            String messageJson = mapper.writeValueAsString(messageDTO);
            // 세션이 존재하면, 반복문을 통해 각 세션에 대해 메시지를 전송함.
            int isreceiver = 0;
            if (sessions != null) {
                for (WebSocketSession s : sessions) {
                    // 현재 순회 중인 세션 s가 메시지를 보낸 사용자(발신자)의 세션과 같은지 확인
                    // 발신자 본인에게는 원본 메시지(message.getPayload())를 전송
                    if(s == session) {
                        s.sendMessage(new TextMessage(message.getPayload()));
                    } else {
                        // 채팅방의 다른 사용자에게는 messageJson을 전송
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
                TextMessage dateMessage = new TextMessage("{\"type\": \"date\", \"date\": \"" + today + "\"}");
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

    // 웹소켓과 연결을 끊을 때 처리하는 기능
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // sessionRoomMap에서 방 ID를 가져와 해당 방의 세션 집합에서 세션을 제거
        Integer roomId = sessionRoomMap.get(session);
        if (roomId != null) {
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            if (sessions != null) {
                sessions.remove(session);
                log.info("Session {} removed from room {}", session.getId(), roomId);
            }
        }
        // 세션의 방 매핑도 제거
        // 연결이 종료되면 sessionRoomMap에서도 세션 정보를 삭제하여 더 이상 필요 없는 세션을 정리함
        sessionRoomMap.remove(session);
        super.afterConnectionClosed(session, status);
        log.info("웹소켓 연결 끊김~~~");
    }
}