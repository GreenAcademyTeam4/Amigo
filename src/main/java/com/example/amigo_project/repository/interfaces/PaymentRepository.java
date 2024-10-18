package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.payment.ChargeHistoryDTO;
import com.example.amigo_project.repository.model.ChargeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentRepository {

    // 결제 내역 생성
    public void createChargeHistory(ChargeHistory chargeHistory);

    // 포인트 충전
    public void chargePoint(ChargeHistory chargeHistory);

    // 상품 구매
    // 상품 선택 --> 구매 --> 포인트 사용 --> 구매 완료


    // 환불


    // 결제 내역 조회(리스트 및 페이징 처리)
    public List<ChargeHistoryDTO> readChargeHistory(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("userId") Integer userId);

    // 결제 내역 개수(페이징 처리)
    public Integer countChargeHistory(@Param("userId") Integer userId);


    // 환불 내역(리스트 및 페이징 처리)
    
    // 중복 결제 확인

    
}
