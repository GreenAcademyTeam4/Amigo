package com.example.amigo_project.service;

import com.example.amigo_project.dto.BoardDTO;
import com.example.amigo_project.repository.interfaces.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void InsertBoard(BoardDTO dto) {

        int number = boardRepository.InsertBoard(dto);

        if(number == 1) {
            System.out.println("등록성공");
        } else {
            System.out.println("등록실패");
        }
    }

    @Transactional(readOnly = true)
    public List<BoardDTO> getBoardsBySchoolId(int schoolId) {
        return boardRepository.findBoardsBySchoolId(schoolId);
    }


    @Transactional(readOnly = true)
    public BoardDTO getBoardById(int boardId) {
        return boardRepository.BoardById(boardId);
    }

    public List<byte[]> getImageById(int schoolId) {
        return boardRepository.findImageSearch(schoolId);
    }


}
