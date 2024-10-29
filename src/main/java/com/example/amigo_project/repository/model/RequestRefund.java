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
public class RequestRefund {

    // 환불 요청 시 담을 model
    private int id;
    private int chargeHistoryId;
    private String cancelReason;
    private Timestamp requestAt;

}
