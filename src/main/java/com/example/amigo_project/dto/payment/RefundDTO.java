package com.example.amigo_project.dto.payment;

import lombok.*;

import java.sql.Timestamp;

@Data
public class RefundDTO {

    // 결제 취소 완료된 내역이 여기에 담김(결제 취소 내역 조회를 위함)
    private int userId; // user_tb의 PK
    private String orderName; // 주문 이름
    private int point;
    private int cancelAmount; // 취소한 금액
    private String cancelReaseon; // 취소 사유
    private Timestamp requestAt; // 취소 요청 시간
    private Timestamp canceledAt; // 취소 완료 시간
}
