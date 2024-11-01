package com.example.amigo_project.dto;

import lombok.Data;

import java.sql.Timestamp;

// 관리자 - 결제 조회
@Data
public class PayListDTO {

    private Integer id;
    private String name;
    private String orderName; // 주문 이름
    private String orderId; // 주문번호
    private Integer totalAmount;
    private Integer point;
    private Timestamp approvedAt;
    private String method; // 결제 방법


}
