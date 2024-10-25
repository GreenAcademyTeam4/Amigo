package com.example.amigo_project.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminDTO {
    private Integer id;
    private String userId;
    private String password;
    private String nickname;
    private Integer schoolId;
    private String title;
    private String contentLocaion;
    // 게시글 개수 조회
    private Integer boardCount;
    private Timestamp createdAt;
}
