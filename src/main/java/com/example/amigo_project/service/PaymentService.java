package com.example.amigo_project.service;

import com.example.amigo_project.dto.payment.ApproveDTO;
import com.example.amigo_project.dto.payment.ChargeHistoryDTO;
import com.example.amigo_project.repository.interfaces.PaymentRepository;
import com.example.amigo_project.repository.model.ChargeHistory;
import com.example.amigo_project.repository.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final PaymentRepository paymentRepository;
    private final HttpSession session;

    @Value("${payments.toss.test_client_api_key}")
    private String testClientKey;
    @Value("${payments.toss.test_secret_api_key}")
    private String testSecretKey;

    /**
     * 서버 -> 카드사 등으로 결제 승인 요청
     */
    @Transactional
    public ChargeHistory requestPayment(ApproveDTO dto) throws IOException, InterruptedException {

        // 사용자가 결제 요청 후 받은 값을 ApproveDTO에 담음
        User user = (User) session.getAttribute("principal");
        int userId = 1; // TODO - 삭제 예정
//        int userId = user.getUserId; // TODO - 주석 해제 예정
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
        System.out.println(response.body());
        
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
     * 결제 내역 생성
     */
    @Transactional
    public void createChargeHistory(ChargeHistory chargeHistory) {
        paymentRepository.createChargeHistory(chargeHistory);
    }

    /**
     * 결제 내역 조회(페이징 처리)
     */
    public List<ChargeHistoryDTO> readChargeHistory(int page, int size, int userId) {
        List<ChargeHistoryDTO> list = new ArrayList<>();
        int limit = size;
        int offset = (page -1) * size;
        list = paymentRepository.readChargeHistory(limit, offset, userId);
        System.out.println("list: " + list); // TODO - 삭제 예정
        return list;
    }

    /**
     * 결제 내역 개수(페이징 처리)
     */
    public Integer countChargeHistory(int userId) {
        return paymentRepository.countChargeHistory(userId);
    }



}
