package com.example.amigo_project.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NoticeDTO {

    private Integer id;
    private String title;
    private String content;
    private int viewCount;
   // private Timestamp createdAt;
}
