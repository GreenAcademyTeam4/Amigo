package com.example.amigo_project.dto;

import lombok.Data;

import java.sql.Timestamp;

// 유저 신고
@Data
public class UserReportDTO {

    private int reportId;
    private String senderName;
    private String receiverName;
    private String category;
    private String content;
    private Timestamp createdAt;
}
