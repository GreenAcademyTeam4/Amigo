package com.example.amigo_project.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class WithdrawalReasonDTO {

    private Integer id;
    private String userName;
    private String reason;
    private String details;
    private Timestamp createdAt;

}
