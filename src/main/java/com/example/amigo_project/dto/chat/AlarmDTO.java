package com.example.amigo_project.dto.chat;

import lombok.Data;

@Data
public class AlarmDTO {

    private String type;
    private Integer senderId;
    private Integer receiverId;
    private Integer content;

}
