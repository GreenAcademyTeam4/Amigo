package com.example.amigo_project.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AlarmDTO {

    private int senderId;
    private int receiverId;
    private String senderName;
    private byte[] senderProfile;
    private String content;
    private Timestamp createdAt;

}
