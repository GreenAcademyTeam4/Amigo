package com.example.amigo_project.service;

import com.example.amigo_project.dto.payment.ChargeHistoryDTO;
import com.example.amigo_project.dto.payment.RefundRefuseDTO;
import com.example.amigo_project.dto.payment.RequestApproveDTO;
import com.example.amigo_project.dto.payment.RequestRefundListDTO;
import com.example.amigo_project.repository.interfaces.PaymentRepository;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.payment.ChargeHistory;
import com.example.amigo_project.repository.model.payment.Refund;
import com.example.amigo_project.repository.model.payment.RefundRefuse;
import com.example.amigo_project.repository.model.payment.RequestRefund;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final HttpSession session;

    /**
     * 서버 -> 카드사 등으로 결제 승인 요청
     */
    @Transactional
    public ChargeHistory requestPayment(RequestApproveDTO dto) throws IOException, InterruptedException {

        // 사용자가 결제 요청 후 받은 값을 ApproveDTO에 담음
        User user = (User) session.getAttribute("principal");
        String paymentKey = dto.getPaymentKey();
        String orderId = dto.getOrderId();
        int amount = dto.getAmount();

        // 헤더 + 바디
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", "Basic dGVzdF9za180eUtlcTViZ3JwUDdlV2dXenE0eHJHWDBselc2Og==")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(String.format("{\"paymentKey\":\"%s\",\"orderId\":\"%s\",\"amount\":%d}", paymentKey, orderId, amount)))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // 결제 요청 승인 후 받은 JSON으로 온 데이터를 모델에 담음
        ObjectMapper objectMapper = new ObjectMapper();
        ChargeHistory chargeHistory = objectMapper.readValue(response.body(), ChargeHistory.class);

        // 임시 데이터 삽입
        chargeHistory.setUserId(user.getId());
        int point = chargeHistory.getTotalAmount(); // 충전한 금액(포인트)만
        chargeHistory.setPoint(point);

        return chargeHistory;
    }

    /**
     * 포인트 충전
     */
    @Transactional
    public void chargePoint(ChargeHistory chargeHistory){
        paymentRepository.chargePoint(chargeHistory);
    }

    /**
     * 포인트 차감
     */
    @Transactional
    public void deductPoint(ChargeHistory chargeHistory){
        paymentRepository.deductPoint(chargeHistory);
    }

    /**
     * 결제 내역 생성
     */
    @Transactional
    public void createChargeHistory(ChargeHistory chargeHistory) {
        paymentRepository.createChargeHistory(chargeHistory);
    }

    /**
     * 전체 결제 내역 조회(페이징 처리)
     */
    public List<ChargeHistoryDTO> readChargeHistory(int page, int size, int userId) {
        int limit = size;
        int offset = (page -1) * size;
        List<ChargeHistoryDTO> list = paymentRepository.readChargeHistory(limit, offset, userId);
        return list;
    }

    /**
     * 결제 내역 개수(페이징 처리)
     */
    public Integer countChargeHistory(int userId) {
        return paymentRepository.countChargeHistory(userId);
    }

    /**
     * 관리자 - 환불 신청 내역 조회(페이징 처리)
     */
    @Transactional
    public List<RequestRefundListDTO> readAllRequestRefund(int page, int size) {
        int limit = size;
        int offset = (page -1) * size;
        List<RequestRefundListDTO> list = paymentRepository.readAllRequestRefund(limit, offset);
        System.out.println("paymentService에서 readAllRequestRefund의 list: " + list);
        return list;
    }

    /**
     * 환불 신청 개수(페이징 처리)
     */
    public Integer countRequestRefundHistory() {
        return paymentRepository.countRequestRefundHistory();
    }

    /**
     * id로 결제 내역 조회
     */
    public ChargeHistory readChargeHistoryById(int id){
        return paymentRepository.readChargeHistoryById(id);
    }

    /**
     * 사용자 - 환불 요청 등록
     */
    @Transactional
    public void createRequestRefund(RequestRefund requestRefund) {
        paymentRepository.createRequestRefund(requestRefund);
    }


    /**
     * 결제 내역에서 환불 상태 변경
     */
    @Transactional
    public void modifyRefundStatus(int id, String refundStatus) {
        paymentRepository.modifyRefundStatus(id, refundStatus);
    }


    /**
     * 관리자 - 결제 취소(환불) 기능
     */
    @Transactional
    public Refund refundCharge(RequestRefundListDTO dto) throws IOException, InterruptedException {

        // 사용자가 입력한 cancelReason 받아오기
        String cancelReason = dto.getCancelReason();

        ChargeHistory readChargeHistory = paymentRepository.readChargeHistoryById(dto.getChargeHistoryId());

        String paymentKey = readChargeHistory.getPaymentKey();

        // 인증 토큰 생성
        String apiKey = "test_sk_4yKeq5bgrpP7eWgWzq4xrGX0lzW6"; // 시크릿키
        String encodedAuth = Base64.getEncoder().encodeToString((apiKey + ":").getBytes(StandardCharsets.UTF_8));

        // 결제 취소 API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        Refund refund = objectMapper.readValue(response.body(), Refund.class); //JSON 데이터를 Refund model에 담음
        System.out.println("refund: " + refund);

        // Toss Payments API의 응답을 확인하여 이미 취소된 상태인지 체크
        if (response.body().contains("\"code\":\"ALREADY_CANCELED_PAYMENT\"")) {
            throw new IllegalStateException("이미 취소된 결제입니다.");
        }
        return refund;
    }

    /**
     * 환불 성공 시 환불 사유 내역 삭제
     */
    @Transactional
    public void removeRequestRefund(int chargeHistoryId) {
        paymentRepository.removeRequestRefund(chargeHistoryId);
    }

    /**
     * 환불 반려 시 환불 반려 사유 생성
     */
    @Transactional
    public void createRefuseReason(RefundRefuse refundRefuse) {
        paymentRepository.createRefuseReason(refundRefuse);
    }

    /**
     * 환불 반려 시 환불 반려 사유 상세보기
     */
    public RefundRefuseDTO readRefuseReasonDetail(int chargeHistoryId) {
        return paymentRepository.readRefuseReasonDetail(chargeHistoryId);
    }

    /**
     * 환불 완료 시 환불 내역 생성
     */
    @Transactional
    public void createRefund(Refund refund) {
        paymentRepository.createRefund(refund);
    }


    /**
     * id로 환불 요청 내역 조회
     */
    @Transactional
    public RequestRefund readRequestRefundById(int id) {
        return paymentRepository.readRequestRefundById(id);
    }

}
