package com.example.amigo_project.controller;

import com.example.amigo_project.dto.BoardDTO;

import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.repository.model.Comment;
import com.example.amigo_project.service.BoardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * // http://localhost:8080/board/form
     * @return
     */

    @GetMapping("/form")
    public String handleBoardList(Model model) {

        int user_id = 1; // 유저 아이디 (나중에 유저 세션에서 가져옴)
        int school_id = 1; // 학교 아이디 (나중에 유저 세션에서 가져옴)
        System.out.println();
        model.addAttribute("userId", user_id);
        model.addAttribute("schoolId", school_id);

        System.out.println("userId : " + user_id);
        System.out.println("schoolId : " + school_id);
        System.out.println("여기 통과하는 중?");
        return "views/board/boardForm";
    }

    /**
     * form 태그에서 게시글 작성후 데이터를 DB로 전송
     * @param title
     * @param schoolId
     * @param contentLocation
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
        System.out.println("콘텐츠는 : " + contentLocation);
        // 서비스 계층 호출
        boardService.InsertBoard(dto);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 등록되었습니다!");

        return "redirect:/board/list";  // 등록 후 목록 페이지로 리다이렉트
    }

    /**
     * 학교에 속한 게시글 모두 출력
     * @param
     * @param model
     * @return
     * // http://localhost:8080/board/list
     */
    // 특정 학교의 게시글 목록 조회
    @GetMapping("/list")
    public String getBoardListBySchool(/*@PathVariable("schoolId") int schoolId */ Model model) {
        int schoolId = 1; // 나중에 유저 세션에서 학교 번호를 가져온다.
        List<BoardDTO> boardList = boardService.getBoardsBySchoolId(schoolId);

        System.out.println("boardList : " + boardList);

        for(BoardDTO a : boardList) {
            a.getFormattedCreatedAt();
        }


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
        board.getFormattedCreatedAt();

        List<CommentDTO> comment = boardService.findCommentsByBoardId(boardId);
        // timestamp 전부 포맷시켜주기
        for(CommentDTO a : comment) {
            a.getFormattedCreatedAt();
        }
        
        model.addAttribute("board", board);
        model.addAttribute("comment", comment);
        System.out.println("sangsasebogi : " + board);
        System.out.println("comment : " + comment);

        return "views/board/boardDetail";
    }


    /**
     * 댓글 전송
     */
    @PostMapping("/comment")
    public String insertComment(
            @RequestParam(name = "boardId") int boardId,
            @RequestParam(name = "content") String content,
            RedirectAttributes redirectAttributes) {



        int userId = 1; // 나중에 유저 세션에서 가져옴
        Comment dto = Comment
                .builder()
                .boardId(boardId)
                .userId(userId)
                .contentLocation(content)
                .build();

        boardService.insertComment(dto);
        System.out.println("commentDTO : " + dto);

        redirectAttributes.addFlashAttribute("message", "댓글이 성공적으로 등록되었습니다!");

        // 댓글 등록 후 해당 게시글 상세 페이지로 리다이렉트
        return "redirect:/board/detail/" + boardId;
    }

    /**
     * 게시글 상세보기 페이지에서 "삭제" 버튼 누르면 작동
     * @param boardId
     * @return
     */
    @PostMapping("/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") int boardId) {

        // 먼저 연결된 댓글 삭제
      //  boardService.deleteCommentsByBoardId(boardId);

        // 게시글 삭제
        boardService.deleteBoard(boardId);
        return "redirect:/board/list";
    }

    /**
     * 게시판 상세보기 에서 수정하기 버튼 클릭 시 수정 페이지로 이동한다.
     * @param boardId
     * @param model
     * @return
     */
    @GetMapping("/update/{boardId}")
    public String updateBoard(@PathVariable(name = "boardId") int boardId, Model model) {
        System.out.println("@@@@@@@@@ boardId : " + boardId);

        BoardDTO boardDTO = boardService.findBoardId(boardId);
        System.out.println("updateDTO : " + boardDTO);

        int userId = 1; // 나중에 세션으로 가져와야 하는 것
        int schoolId = 1; // 나중에 세션으로 가져와야 하는 것

        model.addAttribute("userId", userId);
        model.addAttribute("schoolId", schoolId);
        model.addAttribute("board", boardDTO);

        return "views/board/boardUpdateForm";
    }

    /**
     * 게시글 수정하면 작동하는 메서드
     * @param boardId
     * @param title
     * @param schoolId
     * @param userId
     * @param contentLocation
     * @return
     */
    @PostMapping("/update/{boardId}")
    public String updateBoardProc(
            @PathVariable(name = "boardId") int boardId,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "school_id") int schoolId,
            @RequestParam(name = "user_id") int userId,
            @RequestParam(name = "content_location") String contentLocation
    ) {

        boardService.updateBoard(boardId, schoolId, title, contentLocation, userId);
        return "redirect:/board/detail/" + boardId;
    }

    /**
     * 댓글 수정하기
     * @return
     */
    @GetMapping("/reply/update/{boardId}")
    public String updateComment(@PathVariable("boardId") int boardId) {

        BoardDTO board = boardService.findBoardId(boardId);
        System.out.println(" comment board  :" + board);

        return "views/board/";
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/reply/delete/{commentId}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") int commentId) {
        try {
            // 댓글 삭제 처리
            boardService.deleteCommentById(commentId);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false}");
        }
    }


    /**
     * 댓글 수정
     * @param commentId
     * @param commentDTO
     * @return
     */
    @PostMapping("/reply/update/{commentId}")
    @ResponseBody
    public ResponseEntity<?> updateComment(
            @PathVariable("commentId") int commentId,
            @RequestBody CommentDTO commentDTO) {

        try {
            // commentId는 경로에서 받고, content는 JSON body에서 가져옵니다.
            String content = commentDTO.getContent();

            // 댓글 수정 처리
            boardService.updateComment(commentId, content);

            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false}");
        }
    }



}

