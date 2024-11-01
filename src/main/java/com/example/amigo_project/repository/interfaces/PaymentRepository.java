package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.payment.ChargeHistoryDTO;
import com.example.amigo_project.dto.payment.RefundRefuseDTO;
import com.example.amigo_project.dto.payment.RequestRefundListDTO;
import com.example.amigo_project.repository.model.payment.ChargeHistory;
import com.example.amigo_project.repository.model.payment.Refund;
import com.example.amigo_project.repository.model.payment.RefundRefuse;
import com.example.amigo_project.repository.model.payment.RequestRefund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentRepository {

    // 결제 내역 생성
    public void createChargeHistory(ChargeHistory chargeHistory);

    // 결제 내역 조회(리스트 및 페이징 처리)
    public List<ChargeHistoryDTO> readChargeHistory(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("userId") Integer userId);

    // 결제 내역 개수(페이징 처리)
    public Integer countChargeHistory(@Param("userId") Integer userId);

    // id로 거래 내역 조회
    public ChargeHistory readChargeHistoryById(@Param("id") Integer id);

    // 포인트 충전
    public void chargePoint(ChargeHistory chargeHistory);
    
    // 포인트 차감
    public void deductPoint(ChargeHistory chargeHistory);
    
    // 환불 성공 시 환불 사유 내역 삭제
    public void removeRequestRefund(@Param("chargeHistoryId") Integer chargeHistoryId);

    // 환불 반려 시 환불 반려 사유 내역 생성
    public void createRefuseReason(RefundRefuse refundRefuse);

    // 환불 반려 시 환불 반려 사유 상세 내역 보기
    public RefundRefuseDTO readRefuseReasonDetail(Integer chargeHistoryId);

    // 환불 완료 내역 생성
    public void createRefund(Refund refund);

    // 환불 요청 등록
    public void createRequestRefund(RequestRefund requestRefund);

    // 환불 요청 상태 변경
    public void modifyRefundStatus(@Param("id") int id, @Param("refundStatus") String refundStatus);

    // 환불 요청 내역(리스트 및 페이징 처리)
    public List<RequestRefundListDTO> readAllRequestRefund(@Param("limit") Integer limit, @Param("offset") Integer offset);

    // 환불 요청 개수(페이징 처리)
    public Integer countRequestRefundHistory();

    // id로 환불 요청 내역 조회
    public RequestRefund readRequestRefundById(@Param("id") Integer id);

}
