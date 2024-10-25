package com.example.amigo_project.dto;

import lombok.*;

import java.sql.Blob;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdDTO {

    private Integer id;
    private String title;
    private Blob imageLocation;
    private Integer viewCount;
    private Timestamp createdAt;

}
