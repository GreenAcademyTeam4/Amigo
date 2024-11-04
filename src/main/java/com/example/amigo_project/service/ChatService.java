package com.example.amigo_project.service;

import com.example.amigo_project.dto.chat.ChatLogDTO;
import com.example.amigo_project.repository.interfaces.ChatRepository;
import com.example.amigo_project.repository.model.chat.ChatLog;
import com.example.amigo_project.repository.model.chat.ChatRoom;
import com.example.amigo_project.repository.model.chat.Emoticon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    /**
     * 이모티콘 조회
     *
     * @return
     */
    public List<Emoticon> findEmoticonList() {
        return chatRepository.findEmoticonList();
    }

    /**
     * 채팅방 조회 및 생성
     * 
     * @param userId
     * @param friendId
     * @return
     */
    @Transactional
    public ChatRoom getOrCreateChatRoom(int userId, int friendId) {
        // 사용자 id와 친구 id로 기존 채팅방 조회
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

        // 새로운 채팅 로그를 생성
        ChatLog chatLog = new ChatLog();
        chatLog.setRoomId(roomId);
        chatLog.setUserId(userId);
        chatLog.setType(type);
        chatLog.setMessage(message);
        chatLog.setCreatedAt(createdAt);

        // 채팅 로그 저장
        chatRepository.createChatLog(chatLog);
    }
}