package com.example.amigo_project.repository.model.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRefund {

    // 환불 요청 시 담을 model
    private int id; // PK
    private int chargeHistoryId; // charge_history_id의 PK
    private String cancelReason; // 환불 사유
    private Timestamp requestAt; // 환불 요청 시간

}
