package com.example.amigo_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
public class StoreDTO {


    @Data
    public static class nowAvatarDTO{
        private Integer userId;
        private Integer head;
        private Integer top;
        private Integer bottom;
        private Integer shoes;
    }

    @Data
    public static class avatarListDTO{
        private Integer avatarId;
        private Integer type;
        private String avatarName;
        private boolean owned;
        private Integer price;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class pointHistoryDTO{
        private Integer historyId;
        private Integer userId;
        private Integer usePoint;
        private Integer lessPoint;
        private String orderHead;
        private String orderBody;
        private Integer[] prodIdList;
        private Timestamp createdAt;


        public pointHistoryDTO(Integer userId, Integer usePoint, Integer nowPoint, String[] prodNameList, Integer[] prodIdList){
            this.userId = userId;
            this.usePoint = usePoint;
            this.prodIdList = prodIdList;
            lessPoint = nowPoint - usePoint;
            orderHead = prodNameList[0] + "외 " + (prodNameList.length-1) + "건";
            String result = null;
            for(int i = 0; i < prodNameList.length; i++){
                result += prodNameList[i];
                result += " ";
            }
            orderBody = result;
            System.out.println("사용 포인트 : " + this.usePoint +"남은 포인트 : " + lessPoint);
        }
    }

    @Data
    public static class basketDTO{
        private Integer totalPrice;
        private String[] prodNameList;
        private Integer[] prodIdList;
    }







}
