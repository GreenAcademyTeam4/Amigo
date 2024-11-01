package com.example.amigo_project.dto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class UserDTO {

    private Integer id; // (pk) auto_increment
    private String userId;
    private String password;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String gender;
    private Integer birth;
    private Integer point;
    private boolean onlineStatus;
    private String activeStatus;
    private Timestamp createdAt;
    private String base64Profile;

    /**
     * 회원가입 DTO
     */
    @Data
    public static class joinDTO{

        private String userId;
        private String password;
        private String name;
        private String phoneNumber;
        private String gender;
        private Integer birth;
    }
    @Data
    public static class infoDTO{

          private int id;
          private int birth;
          private String gender;
          private String phoneNumber;
          private String name;
    	  private String nickname;
          private String school;
          private String schoolRegion;
          private int schoolId;
    }
    /**
     * 로그인 DTO
     */
    @Data
    public static class loginDTO{
        private String userId;
        private String password;

    }

    @Data
    public class KakaoDTO {
        private long number;
        private String kakaoId;
        private String name;
        private String kakaoPassword;
        public KakaoDTO(String kakaoId, String kakaoPassword){
            this.kakaoId = kakaoId;
            this.kakaoPassword = kakaoPassword;

        }
    }
    
    @Data
    public static class   NaverDTO {
        private long number;
        private String naverId;
        private String name;
        private String naverPassword;
        public NaverDTO(String naverId, String naverPassword){
            this.naverId = naverId;
            this.naverPassword = naverPassword;

        }

        
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleDTO {
        private String id;
        private String name;
        private String email;
        private String googlePassword;

        public GoogleDTO(String email, String googlePassword) {
            this.email = email;
            this.googlePassword = googlePassword;
        }
}

    public byte[] convertFileToBytes(String filePath) throws IOException {
        File file = new File(filePath);
        return Files.readAllBytes(file.toPath());
    }
}