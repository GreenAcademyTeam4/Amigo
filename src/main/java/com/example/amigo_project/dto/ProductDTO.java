package com.example.amigo_project.dto;

// 잘 판린 상품 (관리자)

import lombok.Data;

@Data
public class ProductDTO {

    private Integer productId;
    private Integer totalSold;
    private String productName;

}
