package com.example.amigo_project.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {

    private int id;
    private String type;
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp createdAt;
    private int status;

}
