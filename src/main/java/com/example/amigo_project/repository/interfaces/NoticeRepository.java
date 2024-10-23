package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.repository.model.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeRepository {

    // 공지 조회
    public List<Notice> findAll();

    // 공지 생성
    public int insertNotice(Notice notice);

    // 공지 삭제
    public int deleteById(int id);

    // 공지 수정
    public int updateNotice(Notice notice);
    
}
