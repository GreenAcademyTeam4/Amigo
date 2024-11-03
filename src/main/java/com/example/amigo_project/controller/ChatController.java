package com.example.amigo_project.controller;

import com.example.amigo_project.dto.chat.ChatLogDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.chat.ChatRoom;
import com.example.amigo_project.repository.model.chat.Emoticon;
import com.example.amigo_project.service.ChatService;
import com.example.amigo_project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    /**
     * 채팅방
     * 
     * @param friendId
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/chatRoom/{id}")
    public String chatRoom(@PathVariable(name = "id") int friendId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("principal");

        // 채팅방 조회 및 생성
        ChatRoom room = chatService.getOrCreateChatRoom(user.getId(), friendId);
        // 친구 조회
        User friend = userService.findUser(friendId);
        // 이모티콘 조회
        List<Emoticon>emoticonList = chatService.findEmoticonList();
        // 로그로 방 ID 확인
        log.info("ChatRoom 할당: roomId={} for userId={} and friendId={}", room.getId(), user.getId(), friendId);

        // profileImage가 null인 경우 빈 문자열로 설정
        if (friend.getProfile() == null) {
            // friend.setProfile(""); // 기본 프로필 이미지 설정 로직
        }

        // 방 번호와 상대방 정보, 사용자 정보, 이모티콘 목록 설정
        model.addAttribute("roomKey", room.getId());
        model.addAttribute("opponent", friend);
        model.addAttribute("user", user);
        model.addAttribute("emoticonList", emoticonList);
        return "views/chat/friendChat";
    }

    /**
     * 채팅 로그 조회
     * 
     * @param roomId
     * @return
     */
    @GetMapping("/logs/{roomId}")
    @ResponseBody
    public List<ChatLogDTO> logs(@PathVariable(name = "roomId") int roomId) {
        List<ChatLogDTO> chatLog = chatService.findChatLogById(roomId);
        return chatLog;
     }
}
