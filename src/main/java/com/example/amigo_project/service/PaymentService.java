package com.example.amigo_project.service;

import com.example.amigo_project.dto.payment.*;
import com.example.amigo_project.repository.interfaces.PaymentRepository;
import com.example.amigo_project.repository.model.*;
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
import java.sql.Timestamp;
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
        // int userId = user.getId(); // TODO - 주석 해제 예정
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
        System.out.println(response.body()); // TODO - 삭제 예정
        
        // 결제 요청 승인 후 받은 JSON으로 온 데이터를 모델에 담음
        ObjectMapper objectMapper = new ObjectMapper();
        ChargeHistory chargeHistory = objectMapper.readValue(response.body(), ChargeHistory.class);

        // 임시 데이터 삽입
        chargeHistory.setUserId(1); // TODO - 삭제 예정
        int point = chargeHistory.getTotalAmount(); // 충전한 금액(포인트)만
        chargeHistory.setPoint(point);

        System.out.println("chargeHistory: " + chargeHistory); // TODO - 삭제 예정
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
       System.out.println("paymentService에서 readChargeHistory의 list: " + list); // TODO - 삭제 예정
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

        System.out.println("승인 요청 보냄..");
        System.out.println("RequestRefundListDTO :" + dto); // 여기까지만 refundStatus가 success 가 들어옴.

        // 사용자가 입력한 cancelReason 받아오기
        String cancelReason = dto.getCancelReason();
        System.out.println("cancelReason: " + cancelReason);

        // ChargeHistory 업데이트를 위해 모델 생성
        ChargeHistory chargeHistory = ChargeHistory.builder()
                .id(dto.getChargeHistoryId()) // DTO에서 ChargeHistory의 ID 사용
                .refundStatus(dto.getRefundStatus()) // DTO에서 가져온 refundStatus 사용
                .build();
        System.out.println("!!!!!!!!!chargeHistory:" + chargeHistory);

        // 데이터베이스 업데이트 수행
        paymentRepository.modifyRefundStatus(chargeHistory.getId(), chargeHistory.getRefundStatus());

        ChargeHistory readChargeHistory = paymentRepository.readChargeHistoryById(chargeHistory.getId());
        System.out.println("DB에 refundStatus 가 수정 되었나????:" + readChargeHistory);

        String paymentKey = readChargeHistory.getPaymentKey();

        System.out.println("paymentKey: " + paymentKey);

        System.out.println("=============================여기부터 service refundCharge 값 확인===========");

        // 인증 토큰 생성
        String apiKey = "test_sk_4yKeq5bgrpP7eWgWzq4xrGX0lzW6";
        String encodedAuth = Base64.getEncoder().encodeToString((apiKey + ":").getBytes(StandardCharsets.UTF_8));

        // 결제 취소 API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", "application/json")
                //.header("Idempotency-Key", "") // 멱등키 생성 TODO - UUID 생성
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body()); // 여기서 취소된 내역 출력됨


        // Toss Payments API의 응답을 확인하여 이미 취소된 상태인지 체크
        if (response.body().contains("\"code\":\"ALREADY_CANCELED_PAYMENT\"")) {
            throw new IllegalStateException("이미 취소된 결제입니다.");
        }


        // 결제 요청 승인 후 받은 JSON으로 온 데이터를 모델에 담음
        ObjectMapper objectMapper = new ObjectMapper();
        //Refund refund = objectMapper.readValue(response.body(), Refund.class);

        ChargeHistoryDTO chargeHistoryDTO = readChargeListByPaymentKey(readChargeHistory); // paymentKey로 출력된 내역이 여기서 한 번 더 호출되면서 출력됨
        Refund refund = Refund.builder()
                .id(dto.getId()) // 기존 DTO에서 가져온 ID 설정
                .chargeHistoryId(chargeHistoryDTO.getId())
                .orderName(chargeHistoryDTO.getOrderName())
                .orderId(chargeHistoryDTO.getOrderId())
                .paymentKey(chargeHistoryDTO.getPaymentKey())
                .cancelAmount(chargeHistoryDTO.getTotalAmount()) // 취소 금액 설정
                .cancelReaseon(cancelReason)
                .requestAt(chargeHistoryDTO.getApprovedAt()) // 승인된 시간 설정
                .canceledAt(new Timestamp(System.currentTimeMillis())) // 현재 시간을 취소 완료 시간으로 설정
                .cancelStatus("승인 완료") // 예시로 취소 상태를 설정
                .build();
        System.out.println("여기 반드시 확인!!!!!!!refund: " + refund);

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
     * paymentKey로 결제 내역 조회하기
     */
    @Transactional
    public ChargeHistoryDTO readChargeListByPaymentKey(ChargeHistory chargeHistory) throws IOException, InterruptedException {

        String paymentKey = chargeHistory.getPaymentKey();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey))
                .header("Authorization", "Basic dGVzdF9za180eUtlcTViZ3JwUDdlV2dXenE0eHJHWDBselc2Og==")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        // 결제 내역 조회해서 JSON으로 받아온 데이터를 DTO에 담음
        ObjectMapper objectMapper = new ObjectMapper();
        ChargeHistoryDTO chargeHistoryDTO = objectMapper.readValue(response.body(), ChargeHistoryDTO.class);
        chargeHistoryDTO.setId(chargeHistory.getId());
        chargeHistoryDTO.setUserId(chargeHistory.getUserId());
        chargeHistoryDTO.setRefundStatus(chargeHistory.getRefundStatus());

        System.out.println("paymentKey로 조회 !!!!chargeHistoryDTO: " + chargeHistoryDTO);


        return chargeHistoryDTO;
    }

    @Transactional
    public RequestRefund readRequestRefundById(int id) {
        return paymentRepository.readRequestRefundById(id);
    }



}
