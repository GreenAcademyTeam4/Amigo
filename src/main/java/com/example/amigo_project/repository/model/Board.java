package com.example.amigo_project.repository.model;

import lombok.*;

import java.sql.Blob;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class Board {

private Integer id;
private Integer schoolId;
private String title;
private String contentLocation;
private byte[] imageLocation;
private Integer userId;
private Integer viewCount;
private Integer likes;
private Timestamp createdAt;

}
