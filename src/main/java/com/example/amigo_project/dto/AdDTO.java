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

    private int id;
    private String title;
    private byte[] imageLocation;
    private int viewCount;
    private Timestamp createdAt;

}
