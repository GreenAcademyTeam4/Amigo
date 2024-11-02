package com.example.amigo_project.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

/**
 * 관리자 - 학교 순위
 */
public class SchoolUserCountDTO {
    private String schoolName; // 학교 이름
    private int userCount;     // 유저 수
}
