package com.example.amigo_project.dto;

// 게시글 신고

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BoardReportDTO {

    // 신고 ID
    private Integer reportId;

    // 신고자 정보
    private String senderNickname;

    private String reportNickname;

    // 게시글 정보
    private Integer boardId;
    private String boardTitle;

    // 신고 내용
    private String reportCategory;
    private String reportContent;
    private Timestamp reportDate;

}
