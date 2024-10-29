package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.NoticeDTO;
import com.example.amigo_project.repository.model.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeRepository {

    // 공지 조회
    public List<Notice> findAll();

    // 공지 상세보기
    public NoticeDTO findByIdNotice(int id);

    // 공지 생성
    public int insertNotice(NoticeDTO noticeDTO);

    // 공지 삭제
    public int deleteById(int id);

    // 공지 수정
    public int updateNotice(NoticeDTO noticeDTO);

    // 공지 상세보기 클릭 시 조회수 증가
    public void viewCount(@Param("noticeId") int noticeId);

}
