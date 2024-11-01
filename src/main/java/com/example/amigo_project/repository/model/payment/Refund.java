package com.example.amigo_project.repository.model.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Refund {

    // 결제 취소 완료된 내역이 여기에 담김
    private int id; // PK
    private String paymentKey;
    private String orderId;
    private String orderName;
    private Timestamp requestedAt; // 취소 요청 시간
    private List<Cancel> cancels;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cancel {
        private int cancelAmount;
        private String cancelReason;
        private String canceledAt;
        private String cancelStatus;
    }
}
