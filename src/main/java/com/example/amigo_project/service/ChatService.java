package com.example.amigo_project.service;

import com.example.amigo_project.dto.chat.ChatLogDTO;
import com.example.amigo_project.repository.interfaces.ChatRepository;
import com.example.amigo_project.repository.model.chat.ChatLog;
import com.example.amigo_project.repository.model.chat.ChatRoom;
import com.example.amigo_project.repository.model.chat.Emoticon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    /**
     * 채팅 로그 삽입
     */
    public void createChatLog(ChatLog chatLog) {
        chatRepository.createChatLog(chatLog);
    }

    /**
     * roomId로 채팅 로그 조회
     */
    public List<ChatLogDTO> findChatLogById(int roomId){
        return chatRepository.findChatLogById(roomId);
    }

    /**
     * roomId로 마지막 메시지 시간 조회
     */
    public  String findLastMessageDate(int roomId) {
        return chatRepository.findLastMessageDate(roomId);
    }



}
