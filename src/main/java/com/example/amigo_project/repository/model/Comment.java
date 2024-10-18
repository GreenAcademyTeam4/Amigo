package com.example.amigo_project.repository.model;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Comment {

    private int id;
    private int boardId;
    private int userId;
    private String contentLocation;
    private Timestamp createdAt;
}
