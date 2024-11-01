package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.chat.ChatLogDTO;
import com.example.amigo_project.repository.model.chat.ChatLog;
import com.example.amigo_project.repository.model.chat.ChatRoom;
import com.example.amigo_project.repository.model.chat.Emoticon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ChatRepository {

    public List<Emoticon>findEmoticonList();

    // 채팅방 생성
    public void createChatRoom(@Param("userId") int userId, @Param("friendId") int friendId);

    // 사용자 ID와 친구 ID로 채팅방 조회
    public ChatRoom findChatRoomByUserIdANDFriendId(@Param("userId") int userId, @Param("friendId") int friendId);

    // 채팅 로그 삽입
    public void createChatLog(ChatLog chatLog);

    // roomId로 채팅 로그 조회
    public List<ChatLogDTO> findChatLogById(@Param("roomId") int roomId);

    // roomId로 마지막 메시지 시간 조회
    public LocalDate findLastMessageDate(@Param("id") int roomId);

    // roomId로 마지막 메시지 시간 수정
    public void updateLastMessageDate(@Param("id") int roomId, @Param("lastMessageDate") LocalDate lastMessageDate);


}
