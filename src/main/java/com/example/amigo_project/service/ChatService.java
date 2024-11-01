package com.example.amigo_project.service;

import com.example.amigo_project.dto.chat.ChatLogDTO;
import com.example.amigo_project.repository.interfaces.ChatRepository;
import com.example.amigo_project.repository.model.chat.ChatLog;
import com.example.amigo_project.repository.model.chat.ChatRoom;
import com.example.amigo_project.repository.model.chat.Emoticon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public List<Emoticon> findEmoticonList() {
        return chatRepository.findEmoticonList();
    }

    /**
     * 채팅방 생성
     */
    public void createChatRoom(int userId, int friendId) {
        chatRepository.createChatRoom(userId, friendId);
    }

    /**
     * 사용자 ID와 친구 ID로 채팅방 조회
     */
    public ChatRoom findChatRoomByUserIdANDFriendId(int userId, int friendId) {
        return chatRepository.findChatRoomByUserIdANDFriendId(userId, friendId);
    }



    @Transactional
    public ChatRoom getOrCreateChatRoom(int userId, int friendId) {
        // 기존 채팅방 조회
        ChatRoom room = chatRepository.findChatRoomByUserIdANDFriendId(userId, friendId);
        if (room == null) {
            // 채팅방 생성
            chatRepository.createChatRoom(userId, friendId);
            // 생성된 채팅방 조회
            room = chatRepository.findChatRoomByUserIdANDFriendId(userId, friendId);
        }
        return room;
    }



    /**
     * 채팅 로그 삽입
     */
    public void createChatLog(ChatLog chatLog) {
        chatRepository.createChatLog(chatLog);
    }

    /**
     * roomId로 채팅 로그 조회
     */
    public List<ChatLogDTO> findChatLogById(int roomId) {
        return chatRepository.findChatLogById(roomId);
    }

    /**
     * roomId로 마지막 메시지 시간 조회
     */
    public LocalDate findLastMessageDate(int roomId) {
        return chatRepository.findLastMessageDate(roomId);
    }

    /**
     * roomId로 마지막 메시지 시간 수정
     */
    public void updateLastMessageDate(int roomId, LocalDate lastMessageDate) {
        chatRepository.updateLastMessageDate(roomId, lastMessageDate);
    }

    /**
     * 새로운 채팅 로그를 저장
     */
    public void saveChatLog(int roomId, int userId, String type, String message, Timestamp createdAt) {

        // 새로운 채팅 로그를 생성합니다.
        ChatLog chatLog = new ChatLog();
        chatLog.setRoomId(roomId);
        chatLog.setUserId(userId);
        chatLog.setType(type);
        chatLog.setMessage(message);
        chatLog.setCreatedAt(createdAt);
        System.out.println("chatLog: " + chatLog);

        // 채팅 로그를 저장합니다.
        chatRepository.createChatLog(chatLog);

    }

    /**
     * roomId에 따라 날짜와 메시지를 포함한 채팅 로그 조회
     */
    public List<ChatLogDTO> getChatLogsWithDates(int roomId) {
        List<ChatLogDTO> chatLogs = findChatLogById(roomId);
        List<ChatLogDTO> chatLogsWithDates = new ArrayList<>();

        String lastDate = null;

        // 채팅 로그를 순회하면서 날짜를 추가
        for (ChatLogDTO chatLog : chatLogs) {


            // 실제 메시지 로그 추가
            chatLogsWithDates.add(chatLog);
        }

        return chatLogsWithDates;
    }
}