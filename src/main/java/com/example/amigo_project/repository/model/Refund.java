package com.example.amigo_project.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Refund {

    // 결제 취소 완료된 내역이 여기에 담김
    private int id;
    private int chargeHistoryId;
    private String orderName; // 주문 이름
    private String orderId; // 주문 번호
    private String paymentKey;
    private int cancelAmount; // 취소한 금액
    private String cancelReaseon; // 취소 사유 - 모든 사유가 여기에 다 담기도록 하기
    private Timestamp requestAt; // 취소 요청 시간
    private Timestamp canceledAt; // 취소 완료 시간
    private String cancelStatus; // 관리자 - 승인/반려

}
