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
        private String avatarName;
        private Integer type;
    }

    @Builder
    @Data
    public static class headInventoryDTO{

        private Integer avatarId;
        private String avatarName;
    }

    @Builder
    @Data
    public static class topInventoryDTO{

        private Integer avatarId;
        private String avatarName;
    }

    @Builder
    @Data
    public static class bottomInventoryDTO{

        private Integer avatarId;
        private String avatarName;
    }

    @Builder
    @Data
    public static class shoesInventoryDTO {

        private Integer avatarId;
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
        private String school;
    }

    @Data
    @Builder
    public static class friendReqDTO{
        private Integer friendId;
        private String friendName;
    }

    @Data
    public static class myFriendListDTO{
        private Integer friendId;
        private String friendName;
        private String gender;
    }

    @Data
    public static class userSchoolDTO{
        private Integer userId;
        private Integer schoolId;
    }


    // 친구 추천을 받기 위한 정보를 담은 DTO
    @Data
    public static class reccomendFriendDTO{
        private Integer friendId;
        private String friendName;
        private String gender;
    }





}
