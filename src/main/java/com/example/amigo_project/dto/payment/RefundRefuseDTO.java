package com.example.amigo_project.dto.payment;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RefundRefuseDTO {
    //관리자가 반려 버튼 눌렀을 때(환불 거절 시) 사용할 DTO
    private int id;
    private int chargeHistoryId;
    private String action;
    private String refundStatus;
    private String refundRefuseReason;
    private Timestamp createdAt;
}
