package com.example.amigo_project.repository.model;

import lombok.*;

import java.sql.Timestamp;

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
    private String password;
    private String nickname;
    private String phoneNumber;
    private String gender;
    private Integer birth;
    private Integer point;
    private Integer onlineStatus;
    private Integer activeStatus;
    private String userRole;
    private String elementarySchool; // 초등학교
    private String midleSchool;
    private String highSchool; 
    private Timestamp createdAt;


}
