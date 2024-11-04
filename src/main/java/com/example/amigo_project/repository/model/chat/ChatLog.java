package com.example.amigo_project.repository.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLog {
    private int id; // PK
    private int roomId; // chat_room_tb의 PK
    private int userId; // user_tb의 PK
    private String type; // chat, emoticon, dateLog(날짜 로그)
    private String message; // 메시지
    private Timestamp createdAt; // 메시지 보낸 시간
}

