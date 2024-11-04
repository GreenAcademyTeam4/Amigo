package com.example.amigo_project.handler;

import com.example.amigo_project.dto.EquipAvatarDTO;
import com.example.amigo_project.dto.chat.ChatRoomDTO;
import com.example.amigo_project.dto.chat.ChatMessageDTO;
import com.example.amigo_project.dto.chat.MessageDTO;
import com.example.amigo_project.dto.chat.RoomDataDTO;
import com.example.amigo_project.dto.chat.SeatDataDTO;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatHandler extends TextWebSocketHandler {

    // 방 넘버 번호
    Map<WebSocketSession, ChatRoomDTO> school = new ConcurrentHashMap<>();
    // 유저 관리 매니저
    private Map<WebSocketSession,RoomDataDTO> userManage = new ConcurrentHashMap<>();
    // 좌석 관리 매니저
    private Map<RoomDataDTO,List<SeatDataDTO>> seatManage = new ConcurrentHashMap<>();
    // 현재 좌석 기록
    private Map<Integer,Integer> currentSeat = new ConcurrentHashMap<>();

    private final UserService userService;
    // 메시지 처리하는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        RoomDataDTO data = (RoomDataDTO)session.getAttributes().get("roomData");
        User principal = (User)session.getAttributes().get("principal");
        ObjectMapper objectMapper = new ObjectMapper();
        MessageDTO messageDTO = objectMapper.readValue(message.getPayload(),MessageDTO.class);
        // 클라이언트의 메세지가 자리요청일때 처리
        if (messageDTO.getType().equals("seat")) {
            int num = Integer.parseInt(messageDTO.getMessage());
            List<SeatDataDTO> seats = seatManage.get(data);
            // 이동하려는 자리가 빈 자리면 이동 처리
            if(seats.get(num) == null) {
            // 현재 사용자의 기존 자리 찾기
            SeatDataDTO userData = SeatDataDTO.builder().nickname(principal.getNickname()).id(principal.getId()).build();
            int currentSeatIndex = seats.indexOf(userData);
            if (currentSeatIndex != -1) {
                // 기존 자리를 null로 초기화
                seats.set(currentSeatIndex, null);
            }
            // 새로운 자리로 이동
            seats.set(num, userData);
            // 좌석 정보 업데이트
            seatManage.put(data, seats);
            currentSeat.put(principal.getId(),num);
            // 좌석 정보를 JSON으로 변환
            String seatData = objectMapper.writeValueAsString(seatManage.get(data));
            // 메세지 타입과 좌석 정보를 담은 MessageDTO를 생성
            MessageDTO serverMessage = MessageDTO.builder().type("seat").message(seatData).build();
            // MessageDTO도 JSON으로 변경후 유저들에게 전송
            String messageJSON = objectMapper.writeValueAsString(serverMessage);
                for(WebSocketSession s : userManage.keySet()) {
                    if(userManage.get(s) == data) {
                        // 나와 같은 room에게 있는 사용자들에게 자리 정보 전송
                        s.sendMessage(new TextMessage(messageJSON));
                    }
                }
            } else {
                // 이미 선택한 자리에 유저가 있으면 경고창
                MessageDTO serverMessage = MessageDTO.builder().type("error").message("자리에 이미 유저가 있습니다.").build();
                String messageJSON = objectMapper.writeValueAsString(serverMessage);
                for(WebSocketSession s : userManage.keySet()) {
                    if(userManage.get(s) == data) {
                        // 나와 같은 room에게 있는 사용자들에게 에러 전송
                        s.sendMessage(new TextMessage(messageJSON));
                    }
                }
            }
        } else if (messageDTO.getType().equals("chat")) {
            // 클라이언트의 메세지가 채팅요청일때 처리
            int seatNum = currentSeat.get(principal.getId());
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder().id(seatNum).message(messageDTO.getMessage()).build();
            String chatToJSON = objectMapper.writeValueAsString(chatMessageDTO);
            MessageDTO serverMessage = MessageDTO.builder().type("chat").message(chatToJSON).build();
            String messageJSON = objectMapper.writeValueAsString(serverMessage);
            for(WebSocketSession s : userManage.keySet()) {
                if(userManage.get(s) == data) {
                    // 나와 같은 room에게 있는 사용자들에게 채팅 전송
                    s.sendMessage(new TextMessage(messageJSON));
                }
            }
        } else if (messageDTO.getType().equals("emoticon")) {
            // 클라이언트의 메세지가 이모티콘일때 처리
            int seatNum = currentSeat.get(principal.getId());
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder().id(seatNum).message(messageDTO.getMessage()).build();
            String chatToJSON = objectMapper.writeValueAsString(chatMessageDTO);
            MessageDTO serverMessage = MessageDTO.builder().type("emoticon").message(chatToJSON).build();
            String messageJSON = objectMapper.writeValueAsString(serverMessage);
            for(WebSocketSession s : userManage.keySet()) {
                if(userManage.get(s) == data) {
                    // 나와 같은 room에게 있는 사용자들에게 이모티콘 전송
                    s.sendMessage(new TextMessage(messageJSON));
                }
            }
        }
    }

    // 입장 시 처리하는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("사람 입장!!!!");
        ObjectMapper objectMapper = new ObjectMapper();
        RoomDataDTO data = (RoomDataDTO)session.getAttributes().get("roomData");
        User principal = (User)session.getAttributes().get("principal");
        EquipAvatarDTO equip = userService.equipUserAvatar(principal.getId());
        SeatDataDTO seatDataDTO = SeatDataDTO.builder().id(principal.getId()).nickname(principal.getNickname()).avatar(equip.getUrl()).build();
        // 처음 들어올때 유저 관리 매니저에 저장
        userManage.put(session,data);
        // 방이 없으면 새로생성하고 좌석을 빈 상태로 초기화
        if(seatManage.get(data) == null) {
            List<SeatDataDTO> seats = new ArrayList<>(Collections.nCopies(30, null));
            // 첫번째 좌석에 유저를 배치
            seats.set(0,seatDataDTO);
            // 좌석정보 업데이트
            currentSeat.put(principal.getId(),0);
            seatManage.put(data,seats);
        } else {
            List<SeatDataDTO>seat = seatManage.get(data);
            for(int i = 0; i < seat.size(); i++) {
                if(seat.get(i) == null) {
                    // 비어있는 자리에 유저를 배치
                    seat.set(i,seatDataDTO);
                    currentSeat.put(principal.getId(),i);
                    return;
                }
            }
            // 변경된 좌석정보를 업데이트
            seatManage.put(data,seat);
        }
        String seatData = objectMapper.writeValueAsString(seatManage.get(data));
        MessageDTO message = MessageDTO.builder().type("seat").message(seatData).build();
        String messageJSON = objectMapper.writeValueAsString(message);
        for(WebSocketSession s : userManage.keySet()) {
            if(userManage.get(s) == data) {
                // 나와 같은 room에게 있는 사용자들에게 자리 정보 전송
                s.sendMessage(new TextMessage(messageJSON));
            }
        }
    }
    
    // 퇴장 시 처리하는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RoomDataDTO data = (RoomDataDTO)session.getAttributes().get("roomData");
        User principal = (User)session.getAttributes().get("principal");
        int seatNum = currentSeat.get(principal.getId());
        List<SeatDataDTO> seatList = seatManage.get(data);
        // 유저 관리 매니저에서 제거
        userManage.remove(session);
        // 내가 배치된 좌석을 비우고 퇴장
        seatList.set(seatNum,null);
        seatManage.put(data,seatList);
        // 내 현재 좌석도 비우고 퇴장
        currentSeat.remove(principal.getId());
        // 만약 내가 마지막 퇴장자이면 방을 제거
        if(seatManage.get(data).stream().allMatch(value -> value == null)) {
            seatManage.remove(data);
        } else {
            String seatData = objectMapper.writeValueAsString(seatManage.get(data));
            MessageDTO message = MessageDTO.builder().type("seat").message(seatData).build();
            String messageJSON = objectMapper.writeValueAsString(message);
            for(WebSocketSession s : userManage.keySet()) {
                if(userManage.get(s) == data) {
                    // 나와 같은 room에게 있는 사용자들에게 자리 정보 전송
                    s.sendMessage(new TextMessage(messageJSON));
                }
            }
        }
    }
}
