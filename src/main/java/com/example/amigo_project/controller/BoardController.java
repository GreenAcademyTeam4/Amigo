package com.example.amigo_project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.amigo_project.dto.BoardDTO;
import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.repository.model.*;
import com.example.amigo_project.service.BoardService;
import com.example.amigo_project.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.amigo_project.dto.BoardDTO;
import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.repository.model.Comment;
import com.example.amigo_project.service.BoardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.security.Principal;
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
    private final UserService userService;


    /**
     * 게시글 작성을 위한 form 태그 출력
     * // http://localhost:8080/board/form
     * @return
     */

    @GetMapping("/form")
    public String handleBoardList(Model model) {


        User principal = (User) session.getAttribute("principal");

//        session.setAttribute("userId", principal); // principal의 user ID 저장
        System.out.println("principal.getId() : " + principal.getId());


        User user = (User)session.getAttribute("principal");
        int user_id = user.getId();
        int school_id = (Integer)session.getAttribute("schoolId");

        System.out.println();
        model.addAttribute("userId", user_id);

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
        dto.setContentLocation(dto.removeHtmlTags(contentLocation));  // 텍스트를 그대로 저장
        dto.setImageLocation(imageLocation.getBytes());  // 파일을 BLOB 데이터로 변환
        dto.setViewCount(0);
        dto.setLikes(0);
        System.out.println("콘텐츠는 : " + dto.removeHtmlTags(contentLocation));
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
     @RequestParam(name = "page", defaultValue = "1") Integer page, // 어디서 부터 시작할 건지
     @RequestParam(name = "size", defaultValue = "4") Integer size // 몇번째 부터 끊을 건지
        ) {

        System.out.println("page : " + page);
        System.out.println("size : " + size);


        User principal = (User) session.getAttribute("principal");
        System.out.println("Principal : " + principal);
        int schoolId = (Integer)session.getAttribute("schoolId");

        List<BoardDTO> boardList = boardService.getBoardsBySchoolId2(schoolId, page - 1, size); // Service에서 페이징된 게시글 목록 가져옴
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
        System.out.println("총 페이지 수 : " + totalPages);
        System.out.println("현재 페이지 : " + page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);


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
                                 @RequestParam(name = "page", defaultValue = "1") int page,
                                 @RequestParam(name = "size", defaultValue = "5") int size) {

        System.out.println("page : " + page);
        System.out.println("size : " + size);

        // 세션에서 현재 로그인된 사용자 정보 가져오기
        User principal = (User) session.getAttribute("principal");
        int userId = principal.getId();
        System.out.println("principal : " + userId);

        // 게시글 정보를 가져오기
        BoardDTO board2 = boardService.getBoardById(boardId);


        // 현재 사용자가 게시글의 작성자인지 확인
        boolean isAuthor = userId == board2.getUserId();
        model.addAttribute("isAuthor", isAuthor);  // 작성자 여부 추가





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
        List<CommentDTO> comment = boardService.findCommentsByBoardIdWithPaging(boardId, page-1, size);
        int totalComments = boardService.getTotalCommentsByBoardId(boardId); // 게시글 댓글 총 갯수
        int totalPages = (int) Math.ceil((double) totalComments / size); // 댓글 총 갯수 / 4

        System.out.println("comment 게시글 상세보기 : " + comment);

        // timestamp 전부 포맷시켜주기
        for(CommentDTO a : comment) {
            a.getFormattedCreatedAt();
           if(a.getUserId() == userId) {
               System.out.println("게시글 판별 작동 Controller");
               a.setCommentAuthor(true);
           }
        }

        model.addAttribute("board", board);
        model.addAttribute("comment", comment);
        model.addAttribute("isAuthor", userId == board.getUserId()); // 게시글 작성자인지 여부
        model.addAttribute("board", board);
        model.addAttribute("comment", comment);
        System.out.println("총 페이지 수 : " + totalPages);
        System.out.println("현재 페이지 : " + page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "views/board/boardDetail";
    }


    /**
     * 뷰에서 답글 버튼 선택시 비동기적으로 답글 조회
     */
    @ResponseBody
    @GetMapping("/nested-comment")
    public ResponseEntity<?>findNestedComment(@RequestParam("boardId") int boardId,
                                              @RequestParam("parentId") int parendId){
        List<CommentDTO> replyList = boardService.findNestedComment(boardId, parendId);
        if (replyList.isEmpty()) {
            return ResponseEntity.ok("현재 작성된 답글이 없습니다.");
        }
        return ResponseEntity.ok(replyList);
    }


    /**
     *  대댓글 작성 기능 -> 대댓글 정보 db 삽입 후 새로운 대댓글 정보 조회 후 리턴
     */
    @ResponseBody
    @PostMapping("/post-nested-comment")
    public ResponseEntity<?>postNestedComment(@RequestParam("boardId") int boardId,
                                              @RequestParam("parentId") int parentId,
                                              @RequestParam("content") String content,
                                              HttpSession session){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 답글 작성이 가능합니다.");
        } else {
            Comment dto = Comment.builder()
                    .boardId(boardId)
                    .userId(principal.getId())
                    .parentId(parentId)
                    .contentLocation(content)
                    .build();
            List<CommentDTO> replyList = boardService.insertNestedComment(dto);
            if(replyList == null){
                return ResponseEntity.ok("댓글이 삭제되었거나 답글 작성 중 오류가 발생하였습니다.");
            }
            return ResponseEntity.ok(replyList);
        }


    }


    /**
     * 댓글 전송 (비동기 처리)
     */
    @PostMapping("/comment")
    public ResponseEntity<?> insertComment(@RequestParam(name = "boardId") int boardId,
                                           @RequestParam(name = "content") String content,
                                           HttpSession session) {

        // 세션에서 현재 로그인된 사용자 정보 가져오기
        User principal = (User) session.getAttribute("principal");
        int userId = principal.getId();
        System.out.println("principal : " + userId);
        Comment dto = Comment
                .builder()
                .boardId(boardId)
                .userId(userId)
                .contentLocation(content)
                .build();

        boardService.insertComment(dto);
        System.out.println("commentDTO : " + dto);

        return ResponseEntity.ok("댓글이 성공적으로 등록되었습니다!");
    }

    /**
     * 게시글 상세보기 페이지에서 "삭제" 버튼 누르면 작동
     * @param boardId
     * @return
     */
    @PostMapping("/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") int boardId, Model model

    ) {

//        // 게시글 삭제
        boardService.deleteBoard(boardId);
//        model.addAttribute("msg", "게시글이 성공적으로 삭제되었습니다.");
//        model.addAttribute("url", "/board/detail");
//        return "alert";


        return "redirect:/board/list";
 //       return "views/board/boardList";
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
        User user = (User)session.getAttribute("principal");
        int userId = user.getId();

        model.addAttribute("userId", userId);
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
    public String updateBoardProc(Model model,
            @PathVariable(name = "boardId") int boardId,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "school_id") int schoolId,
            @RequestParam(name = "user_id") int userId,
            @RequestParam(name = "content_location") String contentLocation,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {


        System.out.println("update : " + boardId);

        System.out.println("수정하기 컨트롤러 ");
        boardService.updateBoard(boardId, schoolId, title, contentLocation, userId);

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

            if(a.getUserId() == userId) {
                System.out.println("게시글 판별 작동 Controller");
                a.setCommentAuthor(true);
            }
        }
        model.addAttribute("isAuthor", userId == board.getUserId()); // 게시글 작성자인지 여부


        // 게시글 정보를 가져오기
        BoardDTO board2 = boardService.getBoardById(boardId);
        // 현재 사용자가 게시글의 작성자인지 확인
        boolean isAuthor = userId == board2.getUserId();
        model.addAttribute("isAuthor", isAuthor);  // 작성자 여부 추가



        model.addAttribute("board", board);
        model.addAttribute("comment", comment);
        model.addAttribute("hasLiked", hasLiked);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);


        System.out.println("board : " + board);
        System.out.println("comment : " + comment);

        return "views/board/boardDetail";
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
        System.out.println("댓글 삭제 처리중!!!!!");
        try {
            // 댓글 삭제 처리
            boardService.deleteCommentById(commentId);
            response.put("success", true);
            System.out.println("댓글 삭제 처리완료!!!!!!!!");
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
    public String multiBoard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("principal");
        System.out.println("유저 아이디 : " + user.getUserId());
        int schoolId = (Integer)session.getAttribute("schoolId");
        List<School> schoolList = userService.findUserSchoolList(user.getId());
        schoolId = schoolList.get(0).getId();
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
    @PostMapping("/search")
    public String searchBoard(@RequestBody Map<String, String> params,
                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                              @RequestParam(name = "size", defaultValue = "4") Integer size,
                              Model model) {
        String keyword = params.get("keyword");
        String searchType = params.get("searchType");
        int totalCount = 0;
        List<BoardDTO> searchResults = new ArrayList<>();
        int schoolId = (Integer)session.getAttribute("schoolId");

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

        // 세션에서 현재 로그인된 사용자 정보 가져오기
        User principal = (User) session.getAttribute("principal");
        int userId = principal.getId();
        System.out.println("principal : " + userId);


//        int userId = 1; // 세션에서 가져올 예정
        boardService.addLike(userId, boardId);

        int likeCount = boardService.getLikeCount(boardId);
        boardService.updateLikesCount(boardId, likeCount);

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

        // 세션에서 현재 로그인된 사용자 정보 가져오기
        User principal = (User) session.getAttribute("principal");
        int userId = principal.getId();
        System.out.println("principal : " + userId);

//        int userId = 1; // 세션에서 가져올 예정
        boardService.removeLike(userId, boardId);

        int likeCount = boardService.getLikeCount(boardId);
        boardService.updateLikesCount(boardId, likeCount);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    /**
     * "조회가 많은 게시글" 을 클릭했을 시 조회가 많은 순서대로 게시글이 나열된다.
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/bestview")
    public String boardView(Model model,
            @RequestParam(name = "offset", defaultValue = "1") Integer page, // 어디서 부터 시작할 건지
            @RequestParam(name = "size", defaultValue = "4") Integer size // 몇번째 부터 끊을 건지
    ) {

        System.out.println("1212");

        User principal = (User) session.getAttribute("principal");
        System.out.println("Principal : " + principal);

        int schoolId = (Integer)session.getAttribute("schoolId");

        List<BoardDTO> boardList = boardService.getBoardfindBoardView(schoolId, page-1, size); // Service에서 페이징된 게시글 목록 가져옴
        System.out.println("view view view : " + boardList);
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
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page); // 현재 페이지 (0부터 시작이므로 +1)

        return "views/board/boardViewList";
    }

    /**
     * "댓글이 많은 게시글" 리스트 댓글이 많은 순서대로 게시글이 나열된다.
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/bestcomment")
    public String boardComment(Model model,
                               @RequestParam(name = "offset", defaultValue = "1") Integer page, // 어디서 부터 시작할 건지
                               @RequestParam(name = "size", defaultValue = "4") Integer size // 몇번째 부터 끊을 건지
    ) {

        System.out.println("1212");

        User principal = (User) session.getAttribute("principal");
        System.out.println("Principal : " + principal);

        int schoolId = (Integer)session.getAttribute("schoolId");

        List<BoardDTO> boardList = boardService.getBoardfindBoardCommend(schoolId, page-1, size); // Service에서 페이징된 게시글 목록 가져옴
        System.out.println("view view view : " + boardList);
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

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "views/board/boardCommentList";
    }


    /**
     * "최근에 생성된 게시글"을 리스트 created_at을 기준으로 최근에 생성된 게시글이 나열된다.
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/newBoard")
    public String boardNew(Model model,
                               @RequestParam(name = "offset", defaultValue = "1") Integer page, // 어디서 부터 시작할 건지
                               @RequestParam(name = "size", defaultValue = "4") Integer size // 몇번째 부터 끊을 건지
    ) {

        System.out.println("1212");

        User principal = (User) session.getAttribute("principal");
        System.out.println("Principal : " + principal);

        int schoolId = (Integer)session.getAttribute("schoolId");

        List<BoardDTO> boardList = boardService.getBoardfindBoardNew(schoolId, page-1, size); // Service에서 페이징된 게시글 목록 가져옴
        System.out.println("view view view : " + boardList);
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
        model.addAttribute("currentPage", page); // 현재 페이지 (0부터 시작이므로 +1)

        return "views/board/boardNewList";
    }



    /**
     * "하트(공감)을 많이 받은 게시글"리스트
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/newHeart")
    public String boardHeart(Model model,
                           @RequestParam(name = "offset", defaultValue = "1") Integer page, // 어디서 부터 시작할 건지
                           @RequestParam(name = "size", defaultValue = "4") Integer size // 몇번째 부터 끊을 건지
    ) {

        System.out.println("1212");

        User principal = (User) session.getAttribute("principal");
        System.out.println("Principal : " + principal);

        int schoolId = (Integer)session.getAttribute("schoolId");

        List<BoardDTO> boardList = boardService.getBoardfindBoardHeart(schoolId, page-1, size); // Service에서 페이징된 게시글 목록 가져옴
        System.out.println("view view view : " + boardList);
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

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page); // 현재 페이지 (0부터 시작이므로 +1)

        return "views/board/boardHeartList";
    }


}

