package com.example.amigo_project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MypageDTO {

    private Integer id; // (pk) auto_increment
    private String nickname;
    private String name;
    private String phoneNumber;
    private String gender;
    private Integer birth;
    private Integer point;
    private Integer head;
    private Integer top;
    private Integer bottom;
    private Integer shoes;

    @Builder
    @Data
    public static class inventoryDTO{

        private Integer userId;
        private Integer avatarId;
        private Integer type;
        private String avatarName;

    }

    @Builder
    @Data
    public static class nowAvatarDTO {

        private Integer userId;
        private Integer head;
        private Integer top;
        private Integer bottom;
        private Integer shoes;

    }

    @Data
    @Builder
    public static class statusDTO{

        private Integer id; // (pk) auto_increment
        private String nickname;
        private String elementarySchool;
        private String middleSchool;
        private String highSchool;

    }

    @Data
    @Builder
    public static class friendReqDTO{
        private Integer senderId;
        private String name;
    }


    public static class myFriendListDTO{
        private Integer friendId;
        private String friendName;
    }






}
