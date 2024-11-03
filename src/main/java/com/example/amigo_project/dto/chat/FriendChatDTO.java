package com.example.amigo_project.dto.chat;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class FriendChatDTO {

    private String type;
    private String message;
    private String sender;
    private Timestamp date;
    private int receiver;

}
