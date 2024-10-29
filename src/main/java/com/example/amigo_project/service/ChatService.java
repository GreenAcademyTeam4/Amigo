package com.example.amigo_project.service;

import com.example.amigo_project.repository.interfaces.ChatRepository;
import com.example.amigo_project.repository.model.Emoticon;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public List<Emoticon> findEmoticonList() {
        return chatRepository.findEmoticonList();
    }
}
