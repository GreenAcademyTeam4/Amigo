package com.example.amigo_project.service;

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
}
