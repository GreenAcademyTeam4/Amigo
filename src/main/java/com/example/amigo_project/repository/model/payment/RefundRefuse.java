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
public class RefundRefuse {
    // 관리자가 반려 사유 입력 후 담을 model
    private int id; // PK
    private int chargeHistoryId; // charge_history_id의 PK
    private String refundRefuseReason; // 반려 사유
    private Timestamp createdAt; // 반려 사유 입력 시간

}
