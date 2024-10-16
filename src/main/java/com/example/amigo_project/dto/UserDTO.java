package com.example.amigo_project.dto;

import lombok.Data;

import java.sql.Timestamp;
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
    private Integer onlineStatus;
    private Integer activeStatus;
    private Timestamp createdAt;

    /**
     * 회원가입 DTO
     */
    @Data
    public static class joinDTO{

        private String userId;
        private String password;
        private String nickname;
        private String name;
        private String phoneNumber;
        private String gender;
        private Integer birth;
    }

    /**
     * 로그인 DTO
     */
    @Data
    public static class loginDTO{
        private String userId;
        private String password;

    }

}
