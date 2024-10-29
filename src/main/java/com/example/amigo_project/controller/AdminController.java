package com.example.amigo_project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.amigo_project.dto.AdminDTO;
import com.example.amigo_project.dto.BoardDTO;
import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.dto.NoticeDTO;
import com.example.amigo_project.repository.model.Board;
import com.example.amigo_project.repository.model.Notice;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.AdminService;
import com.example.amigo_project.service.BoardService;
import com.example.amigo_project.service.NoticeService;
import com.example.amigo_project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final BoardService boardService;
    private final NoticeService noticeService;
    private final PaymentService paymentService; // 결제




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
     * 결제 관리
     */
//    @GetMapping("/user")
//    public String userPage(Model model){
//        List<User> userList = adminService.getUserList();
//
//        model.addAttribute("userList", userList);
//        return "views/admins/user"; // 임시
//    }



    
    

    // 광고 관리


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

    // 문의 관리

    // 신고 관리

    // 통계
    @GetMapping("/statistic")
    public String statisticForm(){
        return "views/admins/statistic";
    }

}
