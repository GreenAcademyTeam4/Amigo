package com.example.amigo_project.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRefundListDTO {
    // 관리자 - 환불 요청 내역 조회
    private int id;
    private int chargeHistoryId;
    private String orderId;
    private String orderName;
    private String paymentKey;
    private int cancelAmount;
    private String cancelReason;
    private Timestamp requestAt;
    private String refundStatus;
    private String cancelStatus; // 관리자 - 승인/반려
    private boolean isRefundable;
}
