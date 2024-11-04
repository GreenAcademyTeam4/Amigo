package com.example.amigo_project.dto.chat;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FriendChatDTO {

    private String type; // roomKey, chat, emoticon
    private String message;
    private String sender;
    private Timestamp date;
    private int receiver;

}
