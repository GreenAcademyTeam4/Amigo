package com.example.amigo_project.repository.model.chat;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatRoom {

    private int id;
    private int userId;
    private int friendId;
    private LocalDate lastMessageDate;

}
