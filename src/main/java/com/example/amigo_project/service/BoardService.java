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
     * 게시글 상세보기 클릭 시 조회수 +1 한다.
     * @param boardId
     */
    public void incrementViewCount(int boardId) {
        boardRepository.incrementViewCount(boardId);
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
     * board_view 테이블에서 유저Id 와 게시판id를 검색한다.
     * @param boardId
     * @return
     */
    public int boardViewCount(int boardId, int userId) {
        return boardRepository.boardViewCount(boardId, userId);
    }
}
