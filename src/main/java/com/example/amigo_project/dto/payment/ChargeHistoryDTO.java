package com.example.amigo_project.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeHistoryDTO {

    private int userId; // user_tb의 PK
    private String name; // 사용자 이름
    private String orderName; // 구매 상품
    private String orderId; // 주문 번호
    private int totalAmount; // 결제 금액
    private Timestamp approvedAt; // 결제 일시
    private String method; // 결제 방식
    private String paymentKey;

}
