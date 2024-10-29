package com.example.amigo_project.controller;

import com.example.amigo_project.dto.BoardDTO;
import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.repository.model.Board;
import com.example.amigo_project.repository.model.Comment;
import com.example.amigo_project.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


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
            @RequestParam("imageLocation") MultipartFile imageLocation,
            RedirectAttributes redirectAttributes) throws IOException {

        // DTO 생성 및 데이터 설정
        BoardDTO dto = new BoardDTO();
        dto.setTitle(title);
        dto.setSchoolId(schoolId);
        dto.setUserId(userId);
        dto.setContentLocation(contentLocation);  // 텍스트를 그대로 저장
        dto.setImageLocation(imageLocation.getBytes());  // 파일을 BLOB 데이터로 변환
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
    public String getBoardListBySchool(/*@PathVariable("schoolId") int schoolId */ Model model,
     @RequestParam(name = "offset", defaultValue = "0") Integer page, // 어디서 부터 시작할 건지 
     @RequestParam(name = "size", defaultValue = "4") Integer size // 몇번째 부터 끊을 건지
        ) {

        int schoolId = 1; // 나중에 유저 세션에서 학교 번호를 가져온다.

        List<BoardDTO> boardList = boardService.getBoardsBySchoolId2(schoolId, page, size); // Service에서 페이징된 게시글 목록 가져옴
        int totalCount = boardService.getBoardBySchoolCount(schoolId); // 학교에 대한 게시글 갯수 = 12개
        int totalPages = (int) Math.ceil((double) totalCount / size); // 게시글 총 갯수 / 4 --> 12/4 --> 3


        for(BoardDTO a : boardList) {
            // 날짜 formatter
            a.getFormattedCreatedAt();

            // 각 게시글에 대한 좋아요 개수를 최신으로 가져옵니다.
            int likeCount = boardService.getLikeCount(a.getId());
            a.setLikes(likeCount);
        }

        model.addAttribute("boardList", boardList);
        model.addAttribute("schoolId", schoolId);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page + 1); // 현재 페이지 (0부터 시작이므로 +1)

        return "views/board/boardList";  // boardList.mustache를 반환
    }

    /**
     * 게시글 상세 보기
     * @param boardId
     * @param model
     * @return
     */
    @GetMapping("/detail/{id}")
    public String getBoardDetail(@PathVariable("id") int boardId, Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "5") int size) {

        int userId = 1; // 나중에 세션에서 사용자 ID를 가져옴

        // 사용자가 해당 게시글을 조회한 적이 있는지 확인
        if (!boardService.hasViewed(userId, boardId)) {
            System.out.println("방문한적 없음 조회수 증가 쿼리 사용 !!!!");
            // 조회수 증가 +1
            boardService.incrementViewCount(boardId);
            // 조회 기록 추가 (board_view_tb)
            boardService.addViewRecord(userId, boardId);
        }

        // 좋아요 상태 확인
        boolean hasLiked = boardService.existsLike(userId, boardId);
        model.addAttribute("hasLiked", hasLiked);

        // 좋아요 개수 가져오기
        int likeCount = boardService.getLikeCount(boardId);
        model.addAttribute("likeCount", likeCount);



        // 게시글 id를 기준으로 정보 가져오기
        BoardDTO board = boardService.getBoardById(boardId);
        board.getFormattedCreatedAt();
        board.getFormattedImage();

        // 게시글 id를 기준으로 댓글 전부 가져오기
        int offset = page * size;
        List<CommentDTO> comment = boardService.findCommentsByBoardIdWithPaging(boardId, offset, size);
        int totalComments = boardService.getTotalCommentsByBoardId(boardId);
        int totalPages = (int) Math.ceil((double) totalComments / size);

        // timestamp 전부 포맷시켜주기
        for(CommentDTO a : comment) {
            a.getFormattedCreatedAt();
        }
        
        model.addAttribute("board", board);
        model.addAttribute("comment", comment);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);


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
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("commentId") int commentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 댓글 삭제 처리
            boardService.deleteCommentById(commentId);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

    /**
     * 게시판 여러개 띄우기
     * @return
     */
    @GetMapping("/multiBoard")
    public String multiBoard(Model model) {

        int schoolId = 1; // 나중에 세션에서 학교 아이디를 가져온다.

        List<BoardDTO> Heart = boardService.findHeartBoard(schoolId);
        List<BoardDTO> Recomend = boardService.findRecomendBoard(schoolId);
        List<BoardDTO> Search = boardService.findSearchBoard(schoolId);
        List<BoardDTO> createdAt = boardService.findSearchCreatedAt(schoolId);

        // timestamp 전부 포맷시켜주기
        for(BoardDTO a : Heart) {
            a.getFormattedCreatedAt();
        }

        // timestamp 전부 포맷시켜주기
        for(BoardDTO a : Recomend) {
            a.getFormattedCreatedAt();
        }

        // timestamp 전부 포맷시켜주기
        for(BoardDTO a : Search) {
            a.getFormattedCreatedAt();
        }

        // timestamp 전부 포맷시켜주기
        for(BoardDTO a : createdAt) {
            a.getFormattedCreatedAt();
        }

        System.out.println("Heart : " + Heart);
        System.out.println("Recomend : " + Recomend);
        System.out.println("Search : " + Search);
        System.out.println("createdAt : " + createdAt);

        model.addAttribute("Heart", Heart);
        model.addAttribute("Recomend", Recomend);
        model.addAttribute("Search", Search);
        model.addAttribute("createdAt", createdAt);

        return "views/board/boardMultiList";

    }

    /**
     * 게시판에서 검색했을 시 작동하는 기능
     */
    @GetMapping("/search")
    public String searchBoard(@RequestParam("keyword") String keyword ,
                              @RequestParam("searchType") String searchType,
                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                              @RequestParam(name = "size", defaultValue = "4") Integer size,
                              Model model) {

        int totalCount = 0;
        List<BoardDTO> searchResults = new ArrayList<>();
        int schoolId = 1; // 예시로 유저 세션에서 학교 번호를 가져오는 것처럼 설정

        try {
            if (page < 0) {
                page = 0;  // page가 음수인 경우 0으로 설정
            }

            int offset = page * size;
            if (offset < 0) {
                offset = 0;
            }

            // option 에서 선택된 것이 있다면
            switch (searchType) {
                case "nickname": // "닉네임" 검색
                    searchResults = boardService.searchBoardsByNickname(schoolId, keyword, offset, size);
                    totalCount = boardService.countSearchBoardsByNickname(schoolId, keyword);
                    break;

                case "titleContent": // "제목 + 내용" 검색
                    searchResults = boardService.searchBoardsByTitleContent(schoolId, keyword, offset, size);
                    totalCount = boardService.countSearchBoardsByTitleContent(schoolId, keyword);
                    break;

                case "title": // "제목" 검색
                    searchResults = boardService.searchBoardsByKeyword(schoolId, keyword, offset, size);
                    totalCount = boardService.countSearchBoardsByKeyword(schoolId, keyword);
                    break;
               
            }

            int totalPages = (int) Math.ceil((double) totalCount / size); // 검색 했을 시 나오는 게시글 리스트 개수

            for(BoardDTO a : searchResults) { // 게시글 리스트의 생성일자를 Formatter로 변경한다.
                a.getFormattedCreatedAt();
            }

            model.addAttribute("boardList", searchResults);
            model.addAttribute("schoolId", schoolId);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword.trim()); // 추가: 검색어를 모델에 추가
            model.addAttribute("searchType", searchType);

            return "views/board/boardSearch";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "검색 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "views/board/error";
        }
    }


    /**
     * 좋아요 추가 하기
     * @param boardId
     * @return
     */
    @PostMapping("/like/{boardId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addLike(@PathVariable int boardId) {
        int userId = 1; // 세션에서 가져올 예정
        boardService.addLike(userId, boardId);

        int likeCount = boardService.getLikeCount(boardId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    /**
     * 좋아요 삭제 하기
     * @param boardId
     * @return
     */
    @DeleteMapping("/like/{boardId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeLike(@PathVariable int boardId) {
        int userId = 1; // 세션에서 가져올 예정
        boardService.removeLike(userId, boardId);

        int likeCount = boardService.getLikeCount(boardId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }







}

