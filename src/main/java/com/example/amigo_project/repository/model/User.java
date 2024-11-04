package com.example.amigo_project.repository.model;

import lombok.*;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;

/**
 * onlineStatus 접속상태 default 0
 * activeStatus 가입상태(탈퇴) default 0
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class User {

    private Integer id; // (pk) auto_increment
    private String userId;
    private String name;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String gender;
    private Integer birth;
    private Integer point;
    private Integer userRole;
    private byte[] profile;
    private boolean onlineStatus;
    private String activeStatus;
    private String school; 
    private Timestamp createdAt;
    private String createdAtFormat;
    private LocalDate suspensionEndDate;  // 정지 종료 날짜

    public void getFormattedCreatedAt() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createdAtFormat = formatter.format(createdAt);
    }

    public byte[] convertFileToBytes(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        return Files.readAllBytes(resource.getFile().toPath());
    }

    public String base64Encoding(byte[] profile) {
        return Base64.getEncoder().encodeToString(profile);
    }
}
