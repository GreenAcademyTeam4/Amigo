package com.example.amigo_project.controller;

import com.example.amigo_project.dto.*;
import com.example.amigo_project.repository.model.Notice;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.payment.ChargeHistory;
import com.example.amigo_project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final BoardService boardService;
    private final NoticeService noticeService;
    private final PaymentService paymentService; // 결제
    private final UserService userService;




    // 메인 화면
    @GetMapping("/main")
    public String home() {
        return "views/admins/admin"; // index.mustache 파일을 반환 (임시)
    }

    /**
     * 회원관리
     * @param model
     * @return
     */
    // 회원 관리 - 유저 관리 페이지
    @GetMapping("/user")
    public String userPage(Model model){
        List<User> userList = adminService.getUserList();

        model.addAttribute("userList", userList);
        return "views/admins/user"; // 임시
    }



    //    // 유저 탈퇴
    @PostMapping("/deleteUsers/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id){
        adminService.deactivatedUserId(id);
        return "redirect:/admin/user/detail/" + id;
    }
//@PostMapping("/deleteUsers/{id}")
//public ResponseEntity<?> deleteUser(@PathVariable int id) {
//    try {
//        adminService.deactivatedUserId(id); // 사용자를 삭제하는 서비스 호출
//        return ResponseEntity.ok().build(); // 성공 응답 반환
//    } catch (Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 삭제에 실패했습니다.");
//    }
//}

    // 탈퇴 회원 조회
    @GetMapping("/deletedUsers")
    public String deletedUsersPage(Model model){
        List<User> deletedUser = adminService.findDeletedUsers();
        model.addAttribute("deletedUser", deletedUser);
        return "views/admins/deletedUsers";
    }

    // 특정 유저의 탈퇴 사유 조회
    @GetMapping("/deletedUsers/withdrawalReason/{id}")
    @ResponseBody
    public WithdrawalReasonDTO findWithdrawalReason(@PathVariable("id") int id) {
        WithdrawalReasonDTO reason = adminService.findWithdrawalReason(id);
        System.out.println("조회된 탈퇴 사유: " + reason); // 탈퇴 사유 데이터 출력
        return adminService.findWithdrawalReason(id);
    }

    // 탈퇴 해지 요청
    @PostMapping("/restoreUser")
    public ResponseEntity<String> restoreUserStatus(@RequestBody UserDTO userDTO) {
        int result = adminService.restoreUserStatus(userDTO);
        if (result > 0) {
            return ResponseEntity.ok("탈퇴 해지 되었습니다.");
        } else {
            return ResponseEntity.status(400).body("탈퇴 해지에 실패했습니다.");
        }
    }



    // 유저 관리 - 상세보기
    @GetMapping("/user/detail/{id}")
    public String userDetail(Model model, @PathVariable(name = "id") int id){
        User user = adminService.findById(id);
        System.out.println(user);
        // 게시글 개수 조회
        AdminDTO adminDTO = adminService.findBoardCount(id);
        // 댓글 개수 조회
        CommentDTO commentDTO = adminService.findCommentCount(id);
        // 시간
        user.getFormattedCreatedAt();
        model.addAttribute("user", user);
        System.out.println("게시글 수 : " + adminDTO.getBoardCount());
        System.out.println("댓글 수 : " + commentDTO.getCommentCount());
        model.addAttribute("boardCount",adminDTO.getBoardCount());
        model.addAttribute("commentCount",commentDTO.getCommentCount());
        return "views/admins/userDetail"; // 임시

    }

    /**
     * 게시글 관리
     * @param model
     * @return
     */
    // 게시글 관리
    @GetMapping("/board-list")
    public String boardPage(Model model){
        int schoolId = 1;
        List<BoardDTO> boardList = boardService.getBoardsBySchoolId(schoolId);

        // 각 BoardDTO에 대해 createdAt 값을 포맷팅 (getFormattedCreatedAt() 호출하는 반복문)
        for (BoardDTO board : boardList) {
            board.getFormattedCreatedAt();
        }

        model.addAttribute("boardList", boardList);

        return "views/admins/board";
    }

    // 게시글 상세보기 (댓글 포함)
    @GetMapping("/board-list/detail/{id}")
    public String boardDetail(Model model, @PathVariable(name = "id") int boardId) {
        // 게시글 정보 조회
        BoardDTO board = boardService.getBoardById(boardId);

        // 각 BoardDTO에 대해 createdAt 값을 포맷팅
        board.getFormattedCreatedAt();

        model.addAttribute("board", board);

        // 해당 게시글의 댓글 조회
        List<CommentDTO> commentList = boardService.findCommentsByBoardId(boardId);
        // 각 댓글에 대해 createdAt 값 포맷팅
        for (CommentDTO comment : commentList) {
            comment.getFormattedCreatedAt();
        }

        model.addAttribute("commentList", commentList);

        return "views/admins/boardDetail";
    }


    // 게시글 삭제하기
    @PostMapping("/deleteBoard/{id}")
    public String deleteBoard(@PathVariable(name = "id") Integer boardId){
        System.out.println("여기로 옵니다.");
        boardService.deleteBoard(boardId);
        return "redirect:/admin/board-list"; // 게시글 목록 페이지로 이동

        // TODO 나중에 오류 페이지 만들기
    }

    // 댓글 삭제하기
    @DeleteMapping("/deleteComment/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable("id") int id) {
        try {
            // 댓글 삭제 처리
            boardService.deleteCommentById(id);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false}");
        }
    }


    /**
     * 결제
     */
// 결제 내역 조회
    @GetMapping("/chargeHistoryList")
    public String chargeHistoryList(Model model) {
        List<PayListDTO> chargeHistoryList = adminService.findChargeHistoryList();
        System.out.println(chargeHistoryList); // 데이터 확인용 로그 추가

        model.addAttribute("chargeHistoryList", chargeHistoryList);
        return "views/admins/chargeHistoryList"; // 임시
    }







//    // 광고 관리
//    @GetMapping("/ad")
//    public String adPage(Model model){
//        List<AdDTO> ad = adminService.findAd();
//        model.addAttribute("ad", ad);
//    return "views/admins/ad";
//    }



    /**
     * 공지 관리
     * @param model
     * @return
     */
    // 공지 관리
    @GetMapping("/notice")
    public String noticePage(Model model){
        List<Notice> noticeList = noticeService.findAll();
        model.addAttribute("noticeList", noticeList);
        return "views/admins/notice";
    }

    // 공지 생성 페이지로 이동
    @GetMapping("/notice/create")
    public String noticeCreateForm(){
        return "views/admins/noticeCreate";
    }


    // 공지 상세보기 페이지로 이동
    @GetMapping("/notice/detail/{id}")
    public String noticeDetailForm(Model model, @PathVariable(name = "id") int id){

        NoticeDTO noticeDTO = noticeService.findByIdNotice(id);
        noticeService.viewCount(id);
        model.addAttribute("noticeDTO", noticeDTO);

        return "views/admins/noticeDetail";
    }

    // 공지 생성
    @PostMapping("/notice/create")
    public String noticeCreate(@ModelAttribute NoticeDTO noticeDTO){
        noticeService.insertNotice(noticeDTO);
        return "redirect:/admin/notice"; // 공지 목록 페이지로 이동
    }


    // 공지 삭제
    @PostMapping("/deleteNotice/{id}")
    public String deleteNotice(@PathVariable(name = "id") Integer id){
        noticeService.deleteById(id);
        return "redirect:/admin/notice"; // 공지 목록 페이지로 이동
    }

    // 공지 수정 페이지
    @GetMapping("/updateNotice/{id}")
    public String noticeUpdateForm(Model model, @PathVariable(name = "id") int id){
        NoticeDTO noticeDTO = noticeService.findByIdNotice(id);
        model.addAttribute("noticeDTO", noticeDTO);
        return "views/admins/noticeUpdate";
    }

    // 공지 수정
    @PostMapping("/updateNotice/{id}")
    public String updateNotice(@PathVariable(name = "id") int id, @ModelAttribute NoticeDTO noticeDTO) {
        // 전달받은 noticeDTO 객체로 공지 사항 수정
        noticeService.updateNotice(noticeDTO);

        // 수정 후 상세 페이지로 리다이렉트
        return "redirect:/admin/notice/detail/" + id;
    }


    // 신고 관리
    // 유저 신고 조회
    @GetMapping("/userReport")
    public String findReportUser(Model model){
        List<UserReportDTO> userReport = adminService.findReportUser();
        System.out.println("유저 신고 리스트: " + userReport);
        model.addAttribute("userReport", userReport);
        return "views/admins/userReport";
    }



    // 게시글 신고 조회
    @GetMapping("/boardReport")
    public String findReportBoard(Model model){
        List<BoardReportDTO> boardReport = adminService.findReportBoard();
        if (boardReport == null || boardReport.isEmpty()) {
            System.out.println("boardReport 데이터가 비어 있습니다.");
        }
        model.addAttribute("boardReport", boardReport);
        return "views/admins/boardReport";
    }

    @GetMapping("/userReportList/{reportId}")
    public String findUserReportDetail(Model model, @PathVariable(name = "reportId") int reportId) {
        // 신고 상세 정보 가져오기
        UserReportDTO userReportDetail = adminService.findUserReport(reportId);

        // 피신고자 ID를 이용해 신고 횟수 조회
        Integer reportCount = adminService.userReportStatistics(userReportDetail.getReceiverId());

        // 모델에 데이터 추가
        model.addAttribute("userReportDetail", userReportDetail);
        model.addAttribute("reportCount", reportCount);  // 신고 횟수 추가

        return "views/admins/userReportList";  // 상세보기 페이지
    }


    @PostMapping("/deleteReports")
    public ResponseEntity<String> deleteReports(@RequestBody List<Integer> reportIds) {
        for (Integer id : reportIds) {
            adminService.deleteBoardReportByBoardId(id);
        }
        return ResponseEntity.ok("선택된 신고가 삭제되었습니다.");
    }


//    // 특정 유저 신고 받은 횟수 조회
//    @GetMapping("/reportCount/{userId}")
//    public String userReportStatistics(Model model, @PathVariable(name = "userId") int userId){
//        UserReportDTO reportCount = adminService.userReportStatistics(userId);
//        model.addAttribute("reportCount", reportCount);
//        return "views/admins/reportCount";
//    }



    // 통계
    @GetMapping("/statistic")
    public String statisticForm(){
        return "views/admins/statistic";
    }

    /**
     * chart
     */
    // 남녀 성비
    @GetMapping("/gender")
    @ResponseBody
    public List<GenderRatioDTO> getGenderCount(){
        return adminService.findGenderCount();
    }

    // 나이 분포
    @GetMapping("/age")
    @ResponseBody
    public List<UserDTO> getBirthCount(){
        return adminService.findBirthCount();
    }

    // 연별 매출
    @GetMapping("/yearlySales")
    @ResponseBody
    public List<ChargeHistory> getYearlySales(){
        return adminService.getYearlySales();
    }

    // 총 유저 수
    @GetMapping("/totalUsers")
    @ResponseBody
    public Integer getTotalUserCount(){
        return adminService.getTotalUserCount();
    }

    // 총 매출
    @GetMapping("/totalRevenue")
    @ResponseBody
    public Integer getTotalRevenue(){
        return adminService.getTotalRevenue();
    }

    // 총 가입자 수
    @GetMapping("/totalMembers")
    @ResponseBody
    public Integer getTotalMembers(){
        return adminService.getTotalMembers();
    }

    // 하루 방문 수
    @GetMapping("/dailyVisits")
    @ResponseBody
    public Integer getDailyVisits(){
        return adminService.getDailyVisits();
    }

    // 총 게시글 수
    @GetMapping("totalPosts")
    @ResponseBody
    public Integer getTotalPosts(){
        return adminService.getTotalPosts();
    }

    // 총 댓글 수
    @GetMapping("/totalComments")
    @ResponseBody
    public Integer getTotalComments(){
        return adminService.getTotalComments();
    }
    // 탈퇴 유저 수
    @GetMapping("/withdrawnUserCount")
    @ResponseBody
    public Integer getWithdrawnUserCount(){
        return adminService.getWithdrawnUserCount();
    }

    // 가장 유저가 많은 학교 순위
    @GetMapping("/topUserCountSchool")
    @ResponseBody
    public List<SchoolUserCountDTO> getTopUserCountSchool(){
        return  adminService.getTopUserCountSchool();
    }

    // 가장 잘 팔린 상품들(리스트)
    @GetMapping("/bestSellingProducts")
    @ResponseBody
    public List<ProductDTO> getBestSellingProducts(){
        return adminService.getBestSellingProducts();
    }

    // 베스트 상품
    @GetMapping("/bestSellingItem")
    @ResponseBody
    public List<ProductDTO> getBestSellingItem(){
        List<ProductDTO> dtos = adminService.getBestSellingItem();
        System.out.println(dtos);
        return dtos;
    }

}
