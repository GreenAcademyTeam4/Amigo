package com.example.amigo_project.dto;

import lombok.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommentDTO {

    private int id;
    private int boardId;
    private String nickname;
    private String content;
    private Timestamp createdAt;
    private String createdAtFormat;

    public void getFormattedCreatedAt() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createdAtFormat = formatter.format(createdAt);
    }
}