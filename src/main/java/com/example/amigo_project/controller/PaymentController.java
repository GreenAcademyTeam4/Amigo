package com.example.amigo_project.controller;

import com.example.amigo_project.dto.payment.*;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.payment.ChargeHistory;
import com.example.amigo_project.repository.model.payment.Refund;
import com.example.amigo_project.repository.model.payment.RefundRefuse;
import com.example.amigo_project.repository.model.payment.RequestRefund;
import com.example.amigo_project.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final HttpSession session;
    private final PaymentService paymentService;

    /**
     * 포인트 충전 화면
     */
    @GetMapping("/pointCharge")
    public String getPaymentPage(Model model) {
        User user = (User) session.getAttribute("principal");

        String phoneNumber = user.getPhoneNumber();

        // 전화번호에서 '-' 제거
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.replaceAll("[^0-9]", ""); // 숫자가 아닌 모든 문자 제거
        }
        model.addAttribute("phoneNumber", phoneNumber);
        return "/views/payment/pointCharge"; // Mustache 파일 이름
    }


    /**
     * 토스 성공 페이지
     */
    @GetMapping("/success")
    public String getSuccessPage(RequestApproveDTO approvedDTO, Model model) throws IOException, InterruptedException {

        // orderId, paymentKey, amount를 서버에 저장해야 함.
        // paymentKey는 토스 페이먼츠에서 각 주문에 발급하는 고유 키 값이다. 결제 승인, 취소, 조회에 사용된다.

        // 결제 승인 요청
        ChargeHistory result = paymentService.requestPayment(approvedDTO);
        paymentService.createChargeHistory(result); // 결제 내역 저장 완료

        // 구매내역(포인트 충전) update
        paymentService.chargePoint(result);

        User user = (User) session.getAttribute("principal");

        // ChargeHistory에 담긴 값을 ChargeHistoryDTO에 담음
        ChargeHistoryDTO dto = ChargeHistoryDTO.builder()
                .name(user.getName())
                .userId(user.getId())
                .orderName(result.getOrderName())
                .totalAmount(result.getTotalAmount())
                .approvedAt(result.getApprovedAt())
                .orderId(result.getOrderId())
                .method(result.getMethod())
                .paymentKey(result.getPaymentKey())
                .refundStatus(result.getRefundStatus())
                .build();

        // 결제 내역 보여주기 위해 model에 값 담기
        model.addAttribute("payment", dto);
        model.addAttribute("user", user);
        return "/views/payment/success";
    }

    /**
     * 토스 실패 페이지
     */
    @GetMapping("/fail")
    public String getFailPage() {
        return "/views/payment/fail";
    }
  
    /**
     * 환불 상태 변경
     * @param requestRefund
     * @return
     */
    @PostMapping("/modifyRefundStatus")
    @ResponseBody
    public ResponseEntity<String> updateRefundStatus(RequestRefundDTO requestRefund) {
        // DTO에 담긴 값을 model에 담음
        try {
            paymentService.modifyRefundStatus(requestRefund.getChargeHistoryId(),requestRefund.getRefundStatus()); // 환불 상태 update
            RequestRefund refund = RequestRefund.builder().chargeHistoryId(requestRefund.getChargeHistoryId())
                    .cancelReason(requestRefund.getCancelReason())
                    .build();
            paymentService.createRequestRefund(refund); // request_refund_tb로 insert
            return ResponseEntity.ok("환불 요청 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("환불 요청 실패");
        }
    }

    /**
     * 사용자 - 결제 내역 조회
     * 
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/paymentList")
    public String showPaymentPage(Model model,
                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "size", defaultValue = "7") Integer size) {

        User user = (User) session.getAttribute("principal");

        // 결제 내역 조회
        List<ChargeHistoryDTO> paymentList = paymentService.readChargeHistory(page, size, user.getId());

        // 결제 완료 일때만 버튼 활성화
        for (ChargeHistoryDTO history2 : paymentList) {
            history2.setRefundable("결제 완료".equals(history2.getRefundStatus()));
        }

        int totalCount = paymentService.countChargeHistory(user.getId());
        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("paymentList", paymentList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);


        // 페이지네이션 관련 데이터 추가
        model.addAttribute("showPrevious", page > 1);
        model.addAttribute("previousPage", page - 1);
        model.addAttribute("showNext", page < totalPages);
        model.addAttribute("nextPage", page + 1);

        // 현재 페이지 번호와 페이지 수를 사용해 페이지 목록 생성
        List<Map<String, Object>> pages = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            Map<String, Object> pageMap = new HashMap<>();
            pageMap.put("number", i);
            pageMap.put("active", i == page ? "active" : "");
            pages.add(pageMap);
        }
        model.addAttribute("pages", pages);

        return "/views/payment/paymentList"; // Mustache 템플릿 이름
    }


    /**
     * 관리자 - 환불 신청 내역 조회(페이징 처리)
     */
    @GetMapping("/requestRefundList")
    public String readAllRequestRefund(Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        // 결제 내역 조회
        List<RequestRefundListDTO> requestRefundList = paymentService.readAllRequestRefund(page, size);

        // refundStatus가 환불 요청 상태일 때만 조회가 가능하도록 필터링
        List<RequestRefundListDTO> filteredList = requestRefundList.stream()
                .filter(dto -> "request".equals(dto.getRefundStatus()))
                .map(dto -> {
                    dto.setCancelStatus("대기중");
                    return dto;
                })
                .collect(Collectors.toList());

        // 환불 요청중(request) 일때만 환불 신청 버튼 활성화
        for (RequestRefundListDTO history : filteredList) {
            history.setRefundable("request".equals(history.getRefundStatus()));
            if ("request".equals(history.getRefundStatus())) {
                history.setRefundable(true);
            } else {
                history.setRefundable(false);
            }
        }

        int totalCount = paymentService.countRequestRefundHistory();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("requestRefundList", requestRefundList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        // 페이지네이션 관련 데이터 추가
        model.addAttribute("showPrevious", page > 1);
        model.addAttribute("previousPage", page - 1);
        model.addAttribute("showNext", page < totalPages);
        model.addAttribute("nextPage", page + 1);

        // 현재 페이지 번호와 페이지 수를 사용해 페이지 목록 생성
        List<Map<String, Object>> pages = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            Map<String, Object> pageMap = new HashMap<>();
            pageMap.put("number", i);
            pageMap.put("active", i == page ? "active" : "");
            pages.add(pageMap);
        }
        model.addAttribute("pages", pages);

        return "/views/payment/requestRefundList";

    }

    /**
     * 환불 요청 시 잔여 포인트 확인
     */
    @PostMapping("/checkPoint")
    @ResponseBody
    public ResponseEntity<String> checkPoint(@RequestParam(name = "id") int id) {
        User user = (User) session.getAttribute("principal");
        int userPoint = user.getPoint(); 

        // 결제 내역 조회
        ChargeHistory chargeHistory = paymentService.readChargeHistoryById(id);
        int refundAmount = chargeHistory.getTotalAmount(); // 결제 했었던 금액(환불 요청 금액)

        // 포인트가 요청된 금액보다 적으면 에러 반환
        if (userPoint < refundAmount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("포인트가 부족하여 환불을 신청할 수 없습니다.");
        }
        return ResponseEntity.ok("환불 사유를 입력해주세요.");
    }


    /**
     * 환불 사유 입력 폼(사용자)
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/refundForm")
    public String showRefundForm(@RequestParam(name = "id") int id, Model model) {
        ChargeHistory chargeHistory = paymentService.readChargeHistoryById(id);
        model.addAttribute("chargeHistory", chargeHistory);
        return "/views/payment/refundReason";
    }

    /**
     * 환불 거절 사유 입력 폼
     */
    @GetMapping("/refuseReasonForm")
    public String showRefuseReasonForm(@RequestParam(name = "id") int id, Model model) {
        RequestRefund requestRefund = paymentService.readRequestRefundById(id);
        ChargeHistory chargeHistory = paymentService.readChargeHistoryById(requestRefund.getChargeHistoryId());

        model.addAttribute("id", chargeHistory.getId());
        model.addAttribute("chargeHistoryId", requestRefund.getChargeHistoryId()); // 적절한 필드 사용
        model.addAttribute("chargeHistory", chargeHistory);
        model.addAttribute("requestRefund", requestRefund);
        return "/views/payment/requestRefuseReason";
    }


    /**
     * 환불 내역 생성( 관리자가 승인 버튼 누를 시)
     */
    @PostMapping("/requestApprove")
    @ResponseBody
     public ResponseEntity<String> createRefund(@RequestBody RequestRefundListDTO dto) throws IOException, InterruptedException {

        // 결제 취소 처리
        Refund refund = paymentService.refundCharge(dto);

        ChargeHistory chargeHistory = ChargeHistory.builder()
                .id(dto.getChargeHistoryId())
                .refundStatus(dto.getRefundStatus()) 
                .build();

        // 데이터베이스 업데이트 수행
        paymentService.modifyRefundStatus(chargeHistory.getId(), chargeHistory.getRefundStatus());

        // id로 결제 내역 조회
        ChargeHistory readChargeHistory = paymentService.readChargeHistoryById(chargeHistory.getId());

        // 구매내역(포인트 차감) update
        paymentService.deductPoint(readChargeHistory);
        
        // 환불 사유 내역 삭제
        paymentService.removeRequestRefund(readChargeHistory.getId());

        // 이미 취소된 결제가 아닌지 확인
        if ("CANCELED".equalsIgnoreCase(readChargeHistory.getRefundStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 취소된 결제입니다.");
        }

        // 환불 내역 insert
        paymentService.createRefund(refund);

        return ResponseEntity.ok("환불 요청 승인을 성공적으로 완료하였습니다.");
     }

    /**
     * 환불 거절(관리자가 반려 버튼 누를 시)
     */
    @PostMapping("/requestRefuse")
    @ResponseBody
    public ResponseEntity<String> requestRefuse(@RequestBody String body) throws JsonProcessingException {

        // JSON 형식을 DTO에 담음
        ObjectMapper mapper = new ObjectMapper();
        RefundRefuseDTO dto = mapper.readValue(body, RefundRefuseDTO.class);

        // 데이터베이스 업데이트 수행
        paymentService.modifyRefundStatus(dto.getChargeHistoryId(), dto.getRefundStatus());

        ChargeHistory readChargeHistory = paymentService.readChargeHistoryById(dto.getChargeHistoryId());

        // 환불 사유 내역 삭제
        paymentService.removeRequestRefund(dto.getChargeHistoryId());

        if ("CANCELED".equalsIgnoreCase(readChargeHistory.getRefundStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 취소된 결제이거나 반려된 결제입니다.");
        }
        return ResponseEntity.ok("환불 요청이 반려되었습니다.");
    }


    /**
     * 반려 사유 내역 생성
     *
     * @param refundRefuseDTO
     * @return
     */
    @PostMapping("/createRefuseReason")
    @ResponseBody
    public ResponseEntity<String> createRefuseReason(@RequestBody RefundRefuseDTO refundRefuseDTO) {
        try {
            // 현재 시간을 "yyyy-MM-dd HH:mm:ss" 형식으로 포맷된 문자열로 저장
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedCreatedAt = format.format(new Date());

            // 관리자가 입력한 값을 dto에 담아서 데이터를 model에 담아주기
            RefundRefuse refundRefuse = RefundRefuse.builder()
                    .id(refundRefuseDTO.getId())
                    .chargeHistoryId(refundRefuseDTO.getChargeHistoryId())
                    .refundRefuseReason(refundRefuseDTO.getRefundRefuseReason())
                    .createdAt(Timestamp.valueOf(formattedCreatedAt)) // createdAt을 Timestamp가 아닌 포맷된 문자열로 전달
                    .build();

            paymentService.createRefuseReason(refundRefuse);

            return ResponseEntity.ok("반려 사유 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반려 사유 저장 실패");
        }
    }

    /**
     * 환불 거부 사유 상세보기 폼
     */
    @GetMapping("/refuseReasonDetail")
    public String showRefuseReasonDetail(@RequestParam(name = "id") int id, Model model) {
        // 특정 ID의 환불 거부 사유를 조회합니다.
        RefundRefuseDTO refundRefuse = paymentService.readRefuseReasonDetail(id);

        // 조회된 환불 거부 사유를 모델에 추가합니다.
        model.addAttribute("refundRefuse", refundRefuse);

        return "/views/payment/refuseReasonDetail";
    }

    /**
     * 환불 요청 사유 상세보기 폼(관리자 측)
     */
    @GetMapping("/cancelReasonForAdmin")
    public String showCancelReasonForAdmin(@RequestParam(name = "id") int id, Model model) {
        // 환불 요청 사유 조회
        RequestRefund reasonDetail = paymentService.readRequestRefundById(id);

        // 조회된 환불 거부 사유를 모델에 추가합니다.
        model.addAttribute("reasonDetail", reasonDetail);

        return "/views/payment/cancelReasonForAdmin";
    }
}
