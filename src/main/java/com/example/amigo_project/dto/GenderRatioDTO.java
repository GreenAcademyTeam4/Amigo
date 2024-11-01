package com.example.amigo_project.dto;

import lombok.*;

// 남녀 성비
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GenderRatioDTO {
    private String gender;
    private int count;
    private double percentage;
}
