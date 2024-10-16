package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.BoardDTO;
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



}
