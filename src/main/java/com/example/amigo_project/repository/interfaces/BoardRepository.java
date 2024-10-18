package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.BoardDTO;
import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.repository.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardRepository {

    public int InsertBoard(BoardDTO dto);

    List<BoardDTO> findBoardsBySchoolId(int schoolId);

    byte[] findImageByBoardId(int boardId);

    public List<byte[]> findImageSearch(int schoolId);

    BoardDTO BoardById(@Param("boardId") int boardId);

    public int insertComment(Comment comment);

    List<CommentDTO> findCommentsByBoardId(@Param("boardId") int boardId);

    public void deleteBoard(@Param("boardId") int boardId);

    BoardDTO findBoardId(@Param("boardId") int boardId);

    public void updateBoard(@Param("boardId") int boardId, @Param("schoolId") int schoolId, @Param("title") String title, @Param("contentLocation") String contentLocation, @Param("userId") int userId);

    public void deleteCommentById(@Param("id") int id);

    public void updateComment(@Param("commentId") int commentId, @Param("content") String content);

}
