package com.example.amigo_project.service;

import com.example.amigo_project.dto.NoticeDTO;
import com.example.amigo_project.repository.interfaces.NoticeRepository;
import com.example.amigo_project.repository.model.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 모든 공지 조회
    public List<Notice> findAll() {
        List<Notice> notices = noticeRepository.findAll();
        return notices;
    }

    // 공지 생성
    public int insertNotice(NoticeDTO noticeDTO){
        return noticeRepository.insertNotice(noticeDTO);
    }

    // 공지 상세보기
    public NoticeDTO findByIdNotice(int id){
        NoticeDTO noticeDTO = noticeRepository.findByIdNotice(id);

        return noticeDTO;
    }

    // 공지 삭제
    public void deleteById(int id){
        noticeRepository.deleteById(id);
    }

    // 공지 수정
    public int updateNotice(NoticeDTO noticeDTO){
        return noticeRepository.updateNotice(noticeDTO);

    }

    // 공지 상세보기 클릭 시 조회수 증가
    public void viewCount(int noticeId){
        noticeRepository.viewCount(noticeId);
    }
}
