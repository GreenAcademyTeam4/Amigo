package com.example.amigo_project.dto;

import com.example.amigo_project.repository.model.Board;
import lombok.*;

import java.sql.Blob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

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


    private byte[] imageLocation;    // BLOB 필드 (이미지)

    private int viewCount;

    private int likes;

    private Timestamp createdAt;

    private String createdAtFormat;

    private String image;
    // 날짜 포맷
    public void getFormattedCreatedAt() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
        this.createdAtFormat = formatter.format(createdAt);
    }
    
    // 이미지 포맷
    public void getFormattedImage() {
//        image = Base64.getEncoder().encodeToString(imageLocation);
        if (this.imageLocation != null && this.imageLocation.length > 0) {
            this.image = Base64.getEncoder().encodeToString(this.imageLocation);
        } else {
            this.image = null;
        }
    }

    // HTML 태그를 제거하는 메서드
    public String removeHtmlTags(String contentLocation) {
        if (contentLocation == null) {
            return null;
        }
        // HTML 태그를 모두 제거 (기본적으로 모든 HTML 태그 제거) and remove &nbsp;
        return contentLocation.replaceAll("<(/?p[^>]*)>", "").replaceAll("&nbsp;", "");
    }


}
