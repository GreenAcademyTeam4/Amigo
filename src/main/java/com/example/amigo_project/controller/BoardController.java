package com.example.amigo_project.controller;

import com.example.amigo_project.dto.BoardDTO;

import com.example.amigo_project.service.BoardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final HttpSession session;
    private final BoardService boardService;


    /**
     * 게시글 작성을 위한 form 태그 출력
     * @return
     */
    // http://localhost:8080/board/list
    @GetMapping("/list")
    public String handleBoardList(Model model) {

        int user_id = 1; // 유저 아이디
        int school_id = 1; // 학교 아이디
        System.out.println();
        model.addAttribute("userId", user_id);
        model.addAttribute("schoolId", school_id);

        System.out.println("userId : " + user_id);
        System.out.println("schoolId : " + school_id);
        System.out.println("여기 통과하는 중?");
        return "views/board/board";
    }

    /**
     * form 태그에서 게시글 작성후 데이터를 DB로 전송
     * @param title
     * @param schoolId
     * @param contentLocation
     * @param imageFile
     * @param UserId
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/insert")
    public String insertBoard(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "school_id") int schoolId,
            @RequestParam(name = "user_id") int userId,
            @RequestParam(name = "content_location") String contentLocation,
//            @RequestParam("image_location") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        // DTO 생성 및 데이터 설정
        BoardDTO dto = new BoardDTO();
        dto.setTitle(title);
        dto.setSchoolId(schoolId);
        dto.setUserId(userId);
        dto.setContentLocation(contentLocation);  // 텍스트를 그대로 저장
//            dto.setImageLocation(imageFile.getBytes());  // 파일을 BLOB 데이터로 변환
        dto.setViewCount(0);
        dto.setLikes(0);

        // 서비스 계층 호출
        boardService.InsertBoard(dto);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 등록되었습니다!");

        return "redirect:/board/board";  // 등록 후 목록 페이지로 리다이렉트
    }

    /**
     * 학교에 속한 게시글 모두 출력
     * @param schoolId
     * @param model
     * @return
     */
    // 특정 학교의 게시글 목록 조회
    @GetMapping("/board")
    public String getBoardListBySchool(/*@PathVariable("schoolId") int schoolId */ Model model) {
        int schoolId = 1;
        List<BoardDTO> boardList = boardService.getBoardsBySchoolId(schoolId);

        model.addAttribute("boardList", boardList);
        model.addAttribute("schoolId", schoolId);

        System.out.println(boardList.toString());

        return "views/board/boardList";  // boardList.mustache를 반환
    }

    /**
     * 게시글 상세 보기
     * @param boardId
     * @param model
     * @return
     */
    @GetMapping("/detail/{id}")
    public String getBoardDetail(@PathVariable("id") int boardId, Model model) {
        BoardDTO board = boardService.getBoardById(boardId);

        model.addAttribute("board", board);
        System.out.println("sangsasebogi : " + board);

        return "views/board/boardDetail";
    }

    /**
     * 이거 댓글 페이지 만들기
     */
    @GetMapping("/comment")
    public String getBoardComment() {
        return null;
    }


}

