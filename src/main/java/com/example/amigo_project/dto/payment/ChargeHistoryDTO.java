package com.example.amigo_project.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeHistoryDTO {

    // 결제 승인 완료된 값이 여기에 담김.(결제 내역 조회 시 사용)
    private int id;
    private int userId; // user_tb의 PK
    private String name; // 사용자 이름
    private String orderName; // 구매 상품
    private String orderId; // 주문 번호
    private int totalAmount; // 결제 금액
    private Timestamp approvedAt; // 결제 일시
    private String method; // 결제 방식
    private String paymentKey;
    private String refundStatus; // 결제 환불 요청 상태
    private boolean isRefundable;
    private boolean isRefused;

    public String getRefundStatus() {
        if (this.refundStatus == null) {
            return "none";
        }

        switch (this.refundStatus) {
            case "none":
                return "결제 완료";
            case "request":
                return "환불 요청 중";
            case "success":
                return "환불 승인";
            case "fail":
                return "환불 거부";
            default:
                return this.refundStatus; // 그 외의 경우는 현재 상태 반환
        }
    }

    public boolean isRefused() {
        return "fail".equalsIgnoreCase(this.refundStatus);
    }

}
