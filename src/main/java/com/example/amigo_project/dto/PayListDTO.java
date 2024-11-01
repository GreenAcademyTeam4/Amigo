package com.example.amigo_project.dto;

import lombok.Data;

import java.sql.Timestamp;

// 관리자 - 결제 조회
@Data
public class PayListDTO {

    private Integer id;
    private String name;
    private String orderName;
    private String orderId;
    private Integer totalAmount;
    private Integer point;
    private Timestamp approvedAt;
    private String method;
    private String refundStatus;

}
