package com.example.amigo_project.dto.payment;

import lombok.Data;

import java.util.List;

@Data
public class HistoryPageDTO {

    private List<ChargeHistoryDTO>  chargeHistoryDTO;
    private int totalCount;
    private int totalPage;
    private int currentPage;
    private int pageSize;

}
