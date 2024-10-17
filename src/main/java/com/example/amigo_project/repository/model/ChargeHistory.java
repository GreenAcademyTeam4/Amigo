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
public class ChargeHistory {

    // 결제 승인 요청 후 값을 담은 model
    private int id;
    private int userId; // user_tb의 PK
    private String orderName;
    private String orderId;
    private int point; // 포인트
    private int totalAmount; // 최종 결제 금액(현금)
    private Timestamp approvedAt; // 결제 승인 시간
    private String method;
    private String paymentKey;

}
