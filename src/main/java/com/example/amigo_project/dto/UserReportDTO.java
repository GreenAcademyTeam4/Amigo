package com.example.amigo_project.dto;

import lombok.Data;

import java.sql.Timestamp;

// 유저 신고
@Data
public class UserReportDTO {
    private Integer reportId;
    private String category;
    private String content;
    private Timestamp createdAt;

    // 신고자 정보
    private String senderNickname;
    private Integer senderId;

    // 피신고자 정보
    private String receiverNickname;
    private Integer receiverId;

    // 추가 필드
    private Integer id;
    private Integer userId;
    private String nickname;
    private Integer reportCount;




}
