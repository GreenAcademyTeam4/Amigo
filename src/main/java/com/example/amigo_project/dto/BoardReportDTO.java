package com.example.amigo_project.dto;

// 게시글 신고

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BoardReportDTO {

    private Integer id;
    private Integer senderUser;
    private Integer boardId;
    private String category;
    private String content;
    private Timestamp createdAt;

}
