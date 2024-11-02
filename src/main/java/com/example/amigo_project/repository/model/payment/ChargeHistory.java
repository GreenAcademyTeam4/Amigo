package com.example.amigo_project.repository.model.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeHistory {

    // 결제 승인 요청 후 값을 담을 model
    private int id; // PK
    private int userId; // user_tb의 PK
    private String orderName; // 주문 이름
    private String orderId; // 주문 번호
    private int point; // 결제 요청 포인트
    private int totalAmount; // 결제 금액(현금)
    private Timestamp approvedAt; // 결제 승인 시간
    private String method; // 카드, 간편결제, 휴대폰
    private String paymentKey; // 토스에서 제공해주는 key
    private String refundStatus; // 환불 상태(none, request, success, fail)

}
