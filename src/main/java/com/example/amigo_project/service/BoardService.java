package com.example.amigo_project.service;

import com.example.amigo_project.dto.BoardDTO;
import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.repository.interfaces.BoardRepository;
import com.example.amigo_project.repository.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글 작성
     * @param dto
     */
    @Transactional
    public void InsertBoard(BoardDTO dto) {

        int number = boardRepository.InsertBoard(dto);

        if(number == 1) {
            System.out.println("등록성공");
        } else {
            System.out.println("등록실패");
        }
    }

    /**
     * 학교 번호를 기준으로 게시글 리스트를 불러온다.
     * @param schoolId
     * @return
     */
    @Transactional(readOnly = true)
    public List<BoardDTO> getBoardsBySchoolId(int schoolId) {
        return boardRepository.findBoardsBySchoolId(schoolId);
    }

    /**
     * 학교 번호를 기준으로 게시글 리스트를 불러온다. (페이징 처리)
     * @param schoolId
     * @return
     */
    @Transactional
    public List<BoardDTO> getBoardsBySchoolId2(int schoolId, Integer page, Integer size) {
        return boardRepository.findBoardsBySchoolId2(schoolId, page, size);
    }

    /**
     * 학교 번호를 기준으로 게시글의 총 갯수를 불러온다. (페이징 처리)
     */
    public int getBoardBySchoolCount(int schoolId) {
        int totalCount = boardRepository.countBoardsBySchoolId(schoolId);
        return totalCount;
    }

    /**
     * 게시글을 가져오기 위해 게시글의 id를 기준으로 찾는다.
     * @param boardId
     * @return
     */
    @Transactional(readOnly = true)
    public BoardDTO getBoardById(int boardId) {
        return boardRepository.BoardById(boardId);
    }

    public List<byte[]> getImageById(int schoolId) {
        return boardRepository.findImageSearch(schoolId);
    }

    /**
     * 게시글 상세보기에서 댓글 작성
     * @param comment
     */
    public void insertComment(Comment comment) {
        boardRepository.insertComment(comment);
    }

    /**
     * 게시글 상세보기에서 사용할 댓글 불러오기 기능
     * @param boardId
     * @return
     */
    public List<CommentDTO> findCommentsByBoardId(int boardId) {
       return boardRepository.findCommentsByBoardId(boardId);
    }

    /**
     * 게시글 삭제 기능
     * @param boardId
     */
    public void deleteBoard(int boardId) {
        boardRepository.deleteBoard(boardId);
    }

    /**
     * 게시글 id를 기준으로 게시글 데이터를 가져오기
     * @param boardId
     */
    public BoardDTO findBoardId(int boardId) {
       return boardRepository.findBoardId(boardId);
    }

    /**
     * 게시글을 수정을 하게 되면 작동하는 메서드
     * @param boardId
     * @param schoolId
     * @param title
     * @param contentLocation
     * @param userId
     */
    public void updateBoard(int boardId, int schoolId, String title, String contentLocation, int userId) {

        boardRepository.updateBoard(boardId, schoolId, title, contentLocation, userId);
    }


    /**
     * 댓글 삭제 하는 기능
     * @param id
     */
    public void deleteCommentById(int id) {
        boardRepository.deleteCommentById(id);
    }

    /**
     * 댓글 수정하는 기능
     * @param commentId
     * @param content
     */
    public void updateComment(int commentId, String content) {
        boardRepository.updateComment(commentId, content);
    }



    /**
     * 게시글 하트가 제일 많은 게시글 3개 올리기
     * @param schoolId
     * @return
     */
    public List<BoardDTO> findHeartBoard(int schoolId) {
       return boardRepository.findHeartBoard(schoolId);
    }

    /**
     * 게시글 댓글이 제일 많은 게시글 3개 올리기
     * @param schoolId
     * @return
     */
    public List<BoardDTO> findRecomendBoard(int schoolId) {
       return boardRepository.findRecomendBoard(schoolId);
    }

    /**
     * 게시글 조회가 많은 게시글 3개 올리기
     * @param schoolId
     * @return
     */
    public List<BoardDTO> findSearchBoard(int schoolId) {
        return boardRepository.findSearchBoard(schoolId);
    }

    /**
     * 최신순으로 만든 게시글 3개
     * @param schoolId
     * @return
     */
    public List<BoardDTO> findSearchCreatedAt(int schoolId) {
        return boardRepository.findSearchCreatedAt(schoolId);
    }


    /**
     * 게시판에서 '제목' 검색 시 작동하는 기능
     * // 검색어로 게시글 찾기 (페이징)
     * @param schoolId
     * @param keyword
     * @param size
     * @return
     */
    public List<BoardDTO> searchBoardsByKeyword(int schoolId, String keyword, int offset, int size) {
        return boardRepository.searchBoardsByKeyword(schoolId, keyword, offset, size);
    }

    /**
     * 게시판에서 '제목' 검색 시 작동하는 기능
     * 검색된 게시글 총 개수
     * @param schoolId
     * @param keyword
     * @return
     */
    public int countSearchBoardsByKeyword(int schoolId, String keyword) {
        return boardRepository.countSearchBoardsByKeyword(schoolId, keyword);
    }

    /**
     * 게시판에서 '닉네임'으로 검색 시 기능
     * 닉네임으로 게시글 찾기 (페이징)
     * @param schoolId
     * @param keyword
     * @param offset
     * @param size
     * @return
     */
    public List<BoardDTO> searchBoardsByNickname(int schoolId, String keyword, int offset, Integer size) {
        return boardRepository.searchBoardsByNickname(schoolId, keyword, offset, size);
    }

    /**
     * 게시판에서 '닉네임'으로 검색된 게시글 총 개수
     * @param schoolId
     * @param keyword
     * @return
     */
    public int countSearchBoardsByNickname(int schoolId, String keyword) {
        return boardRepository.countSearchBoardsByNickname(schoolId, keyword);
    }


    /**
     * 게시판에서 '제목 + 내용'으로 검색 시 기능
     * @param schoolId
     * @param keyword
     * @param offset
     * @param size
     * @return
     */
    public List<BoardDTO> searchBoardsByTitleContent(int schoolId, String keyword, int offset, int size) {
        return boardRepository.searchBoardsByTitleContent(schoolId, keyword, offset, size);
    }

    /**
     * 게시판에서 '제목 + 내용'으로 검색된 게시글 총 개수
     * @param schoolId
     * @param keyword
     * @return
     */
    public int countSearchBoardsByTitleContent(int schoolId, String keyword) {
        return boardRepository.countSearchBoardsByTitleContent(schoolId, keyword);
    }

    /**
     *
     * @param boardId
     * @param offset
     * @param size
     * @return
     */
    public List<CommentDTO> findCommentsByBoardIdWithPaging(int boardId, int offset, int size) {
        return boardRepository.findCommentsByBoardIdWithPaging(boardId, offset, size);
    }

    /**
     * 게시글 상세보기 클릭 시 게시글에 적힌 댓글 총 개수 구하는 메서드 추가
     * @param boardId
     * @return
     */
    public int getTotalCommentsByBoardId(int boardId) {
        return boardRepository.countCommentsByBoardId(boardId);
    }


    /**
     * 조회수 +1을 하기 위해서 true/ false 로 board_view_tb에 값이 들어가 있는지 확인한다.
     * @param userId
     * @param boardId
     * @return
     */
    public boolean hasViewed(int userId, int boardId) {
        return boardRepository.existsInBoardView(userId, boardId) > 0;
    }

    /**
     * 게시글 상세보기 첫 방문시 조회수 증가 +1
     * @param boardId
     */
    public void incrementViewCount(int boardId) {
        boardRepository.incrementViewCount(boardId);
    }

    /**
     * 게시글 상세보기 첫 방문시 조회 기록 추가 (board_view_tb)
     * @param userId
     * @param boardId
     */
    public void addViewRecord(int userId, int boardId) {
        boardRepository.insertBoardView(userId, boardId);
    }



    /**
     * 좋아요 기능 (+ 증가)
     * @param userId
     * @param boardId
     */
    @Transactional(readOnly = true)
    public void addLike(int userId, int boardId) {
        if (!boardRepository.existsLike(userId, boardId)) {
            boardRepository.insertLike(userId, boardId);
            boardRepository.incrementLikeCount(boardId); // 좋아요 수 증가
        }
    }

    /**
     * 좋아요 기능 (- 기능)
     * @param userId
     * @param boardId
     */
    @Transactional
    public void removeLike(int userId, int boardId) {
        if (boardRepository.existsLike(userId, boardId)) {
            boardRepository.deleteLike(userId, boardId);
            boardRepository.decrementLikeCount(boardId); // 좋아요 수 감소
        }
    }

    /**
     * 좋아요 기능 (몇개인지 개수)
     * @param boardId
     * @return
     */
    @Transactional(readOnly = true)
    public int getLikeCount(int boardId) {
        return boardRepository.countLikes(boardId);
    }


    @Transactional(readOnly = true)
    public boolean existsLike(int userId, int boardId) {
        return boardRepository.existsLike(userId, boardId);
    }
}
