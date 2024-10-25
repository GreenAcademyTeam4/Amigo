package com.example.amigo_project.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Notice {
    private Integer id;
    private String title;
    private String content;
    private Integer viewCount;
    private Timestamp createdAt;
    private String createdAtFormat;

    public void getFormattedCreatedAt() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createdAtFormat = formatter.format(createdAt);
    }

}
