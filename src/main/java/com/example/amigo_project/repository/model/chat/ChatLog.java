package com.example.amigo_project.repository.model.chat;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatLog {
    private int id; // PK
    private int roomId; // chat_room_tb의 PK
    private int userId; // user_tb의 PK
    private String type; // 이모티콘, 메시지
    private String message;
    private Timestamp createdAt;
}

