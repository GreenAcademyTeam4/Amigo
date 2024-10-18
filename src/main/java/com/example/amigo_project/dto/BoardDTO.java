package com.example.amigo_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Blob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardDTO {

    private int id;

    private int schoolId;

    private int userId;

    private String nickname;

    private String title;

    private String contentLocation;  // 게시글 내용은 텍스트이므로 String으로 변경

   // private byte[] imageLocation;    // BLOB 필드 (이미지)

    private int viewCount;

    private int likes;

    private Timestamp createdAt;

    private String createdAtFormat;

    public void getFormattedCreatedAt() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createdAtFormat = formatter.format(createdAt);
    }
}
