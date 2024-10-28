package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.BoardDTO;
import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.repository.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardRepository {

    // 게시글을 작성
    public int InsertBoard(BoardDTO dto);

    // 특정 학교의 게시글을 리스트로 가져온다.
    List<BoardDTO> findBoardsBySchoolId(int schoolId);

    // 특정 학교의 게시글을 리스트로 가져온다. (페이징 처리)
    List<BoardDTO> findBoardsBySchoolId2(@Param("schoolId") int schoolId, @Param("offset") int offset, @Param("size") int size);

    // 특정 학교의 게시글의 리스트의 갯수를 가져온다. (페이징 처리)
    int countBoardsBySchoolId(int schoolId);

    //  학교id를 기준으로 게시글을 리스트로 가져온다.
    public List<byte[]> findImageSearch(int schoolId);

    // 게시글에 대한 상세내용을 가져오면서 user_tb에 있는 nickname도 가져온다.
    BoardDTO BoardById(@Param("boardId") int boardId);

    // 댓글을 입력한다.
    public int insertComment(Comment comment);

    // 게시글 상세보기에서 게시글Id에 속한 모든 댓글을 리스트로 가져온다.
    List<CommentDTO> findCommentsByBoardId(@Param("boardId") int boardId);

    // 게시글id를 기준으로 게시글을 삭제하는 쿼리
    public void deleteBoard(@Param("boardId") int boardId);

    // 게시글id를 기준으로 게시글을 가져온다.
    BoardDTO findBoardId(@Param("boardId") int boardId);

    // 게시글 수정하기 버튼 클릭 후 수정하면 작동
    public void updateBoard(@Param("boardId") int boardId, @Param("schoolId") int schoolId, @Param("title") String title, @Param("contentLocation") String contentLocation, @Param("userId") int userId);

    // 댓글을 삭제
    public void deleteCommentById(@Param("id") int id);

    // 댓글을 수정
    public void updateComment(@Param("commentId") int commentId, @Param("content") String content);

    boolean isLikedByUser(@Param("boardId") int boardId, @Param("userId") int userId);

    void addLike(@Param("boardId") int boardId, @Param("userId") int userId);

    void removeLike(@Param("boardId") int boardId, @Param("userId") int userId);

    // 게시글 하트가 제일 많은 게시글 올리기
    List<BoardDTO> findHeartBoard(@Param("schoolId") int schoolId);

    // 게시글 댓글이 제일 많은 게시글 올리기
    List<BoardDTO> findRecomendBoard(@Param("schoolId") int schoolId);

    // 게시글 조회가 많은 게시글 올리기
    List<BoardDTO> findSearchBoard(@Param("schoolId") int schoolId);

    // 게시글 조회가 많은 게시글 올리기
    List<BoardDTO> findSearchCreatedAt(@Param("schoolId") int schoolId);


    // 검색어로 게시글 찾기 (페이징)
    List<BoardDTO> searchBoardsByKeyword(@Param("schoolId") int schoolId, @Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    // 검색된 게시글 총 개수
    int countSearchBoardsByKeyword(@Param("schoolId") int schoolId, @Param("keyword") String keyword);

    //  게시판에서 닉네임으로 검색
    List<BoardDTO> searchBoardsByNickname(int schoolId, String keyword, int offset, Integer size);

    // 게시판에서 "닉네임"으로 검색된 게시글 총 개수
    int countSearchBoardsByNickname(@Param("schoolId") int schoolId, @Param("keyword") String keyword);

    // "제목" + "내용"으로 검색
    List<BoardDTO> searchBoardsByTitleContent(@Param("schoolId") int schoolId, @Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);
    
    // 게시판에서 "제목"으로 검색된 게시글 총 개수
    int countSearchBoardsByTitleContent(@Param("schoolId") int schoolId, @Param("keyword") String keyword);

    // 게시글 상세보기 클릭 시 기존 댓글 불러오기 메서드 (페이징 처리)
    List<CommentDTO> findCommentsByBoardIdWithPaging(int boardId, int offset, int size);

    // 게시글 상세보기 클릭 시 게시글에 적힌 댓글 총 개수
    int countCommentsByBoardId(int boardId);

    // 사용자가 해당 게시글을 조회했는지 확인하는 쿼리 (조회수)
    int existsInBoardView(@Param("userId") int userId, @Param("boardId") int boardId);

    // 조회 기록을 추가하는 쿼리 (조회수)
    void insertBoardView(@Param("userId") int userId, @Param("boardId") int boardId);

    // 게시글 상세보기 클릭 시 조회수가 +1 증가 (조회수)
    void incrementViewCount(@Param("boardId") int boardId);


    void insertLike(@Param("userId") int userId, @Param("boardId") int boardId);

    void deleteLike(@Param("userId") int userId, @Param("boardId") int boardId);

    boolean existsLike(@Param("userId") int userId, @Param("boardId") int boardId);

    int countLikes(@Param("boardId") int boardId);


    // 좋아요 삭제 기능
    void decrementLikeCount(@Param("boardId") int boardId);

    // 좋아요 추가 기능
    void incrementLikeCount(@Param("boardId") int boardId);
}
