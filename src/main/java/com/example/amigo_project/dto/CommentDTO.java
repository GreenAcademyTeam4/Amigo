package com.example.amigo_project.dto;

import lombok.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommentDTO {

    private int id;
    private int boardId;
    private int userId;
    private int parentId;
    private int replyCount;
    private String nickname;
    private String content;
    private Timestamp createdAt;
    private String createdAtFormat;
    private Integer CommentCount;
    private boolean isCommentAuthor; // 댓글 작성자 여부 확인을 위한 필드 추가

    public void getFormattedCreatedAt() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createdAtFormat = formatter.format(createdAt);
    }


}