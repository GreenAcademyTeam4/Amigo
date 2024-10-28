package com.example.amigo_project.dto;

import lombok.Data;

@Data
public class StoreDTO {



    @Data
    public static class avatarListDTO{
        private Integer avatarId;
        private Integer type;
        private String avatarName;
        private Integer owned;
        private Integer price;
    }


    @Data
    public static class avatarBuyDTO{
        private Integer avatarId;

    }






}
