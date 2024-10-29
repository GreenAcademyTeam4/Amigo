package com.example.amigo_project.dto.payment;

import lombok.*;

@Data
public class RequestRefundDTO {

    // 환불 요청 시 데이터를 담을 DTO
    private int id;
    private int chargeHistoryId;
    private String refundStatus;
    private String cancelReason;
}
