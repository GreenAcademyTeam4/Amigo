package com.example.amigo_project.controller;

import com.example.amigo_project.dto.payment.ApproveDTO;
import com.example.amigo_project.dto.payment.ChargeHistoryDTO;
import com.example.amigo_project.repository.model.ChargeHistory;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final HttpSession session;
    private final PaymentService paymentService;

    /**
     * 포인트 충전 화면
     */
    @GetMapping("/pointcharge")
    public String getPaymentPage(Model model) {
        User user = (User) session.getAttribute("principal");

        // TODO - 주석 해제 예정
        //String phoneNumber = user.getPhoneNumber;
        //model.addAttribute("phoneNumber", phoneNumber);
        //System.out.println("phoneNumber : " + phoneNumber); // TODO - 삭제 예정
        return "/payment/pointcharge"; // Mustache 파일 이름 (payment.mustache)
    }


    /**
     * 토스 성공 페이지
     */
    @GetMapping("/success")
    public String getSuccessPage(ApproveDTO approvedDTO, Model model) throws IOException, InterruptedException {

        // orderId, paymentKey, amount를 서버에 저장해야 함.
        // paymentKey는 토스 페이먼츠에서 각 주문에 발급하는 고유 키 값이다. 결제 승인, 취소, 조회에 사용된다.
        System.out.println("orderId : " + approvedDTO.getOrderId() + " paymentKey : "
                + approvedDTO.getPaymentKey() + " amount : " + approvedDTO.getAmount()); // TODO - 삭제 예정

        // 결제 승인 요청
        ChargeHistory result = paymentService.requestPayment(approvedDTO);
        paymentService.createChargeHistory(result); // 결제 내역 저장 완료

        // 구매내역(포인트 충전) update
        paymentService.chargePoint(result);
        
        User user = (User) session.getAttribute("principal");

        // ChargeHistory에 담긴 값을 ChargeHistoryDTO에 담음
        ChargeHistoryDTO dto = ChargeHistoryDTO.builder()
                //.name(user.getName())
                .orderName(result.getOrderName())
                .totalAmount(result.getTotalAmount())
                .approvedAt(result.getApprovedAt())
                .orderId(result.getOrderId())
                .method(result.getMethod())
                .build();

        // 결제 내역 보여주기 위해 model에 값 담기
        model.addAttribute("payment", dto);
        return "/payment/success";
    }

    /**
     * 토스 실패 페이지
     */
    @GetMapping("/fail")
    public String getFailPage(){
        return "/payment/fail";
    }


}
