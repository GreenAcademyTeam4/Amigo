package com.example.amigo_project.repository.model.chat;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatRoom {

    private int id; // PK(roomId)
    private int userId;
    private int friendId;
    private LocalDate lastMessageDate; // 마지막 메시지 날짜
}
