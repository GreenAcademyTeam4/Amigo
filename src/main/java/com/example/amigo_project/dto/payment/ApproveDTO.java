package com.example.amigo_project.dto.payment;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApproveDTO {

    // 결제 승인 요청 시 사용하는 DTO
    private String orderName; // 주문 이름
    private int amount; // 주문한 금액(현금)
    private String paymentKey;
    private String orderId; // 랜덤으로 생성된
    private String customerMobilePhone; // 계좌 이체에서 휴대폰으로 비밀번호 설정 시 사용

}
