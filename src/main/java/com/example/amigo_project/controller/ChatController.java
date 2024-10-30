package com.example.amigo_project.controller;

import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.chat.ChatRoom;
import com.example.amigo_project.service.ChatService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/chatRoom/{id}")
    public String chatRoom(@PathVariable(name ="id") int friendId, HttpSession session, Model model) {

        User user = (User) session.getAttribute("principal");

        // 친구Id와 내 ID를 받아와서 roomId가 존재하는지 조회
        ChatRoom room = chatService.findChatRoomByUserIdANDFriendId(user.getId(), friendId);
        LocalDate currentDate = LocalDate.now(); // 현재 날짜
        String lastMsgTime = chatService.findLastMessageDate(room.getId()); // 원래 존재하던 방의 마지막 메시지 날짜 조회


        // 만약 방이 비어있을 때
        if (room == null) {
            chatService.createChatRoom(user.getId(), friendId); // userId와 friendId로 새 방을 생성
            room = chatService.findChatRoomByUserIdANDFriendId(user.getId(), friendId); // userId와 friendId로 방 번호 조회
            // 생성한 방 번호를 model에 담음
            model.addAttribute("roomKey", room.getId());
            // 새 방
            model.addAttribute("currentDate", currentDate.toString());

        // 방이 비어 있지 않을 때
        } else {
            // 원래 존재하던 방 번호를 model에 담음
            model.addAttribute("roomKey", room.getId());

            // 마지막 메시지 날짜가 null이 아니거나 비어 있지 않다면
            if (lastMsgTime != null && !lastMsgTime.isEmpty()) {
                // lastMsgTime을 LocalDate로 변환
                LocalDate lastMsgDate = LocalDate.parse(lastMsgTime);

                // 마지막 메시지 날짜와 현재 날짜 비교
                // 마지막 메시지 날짜가 현재 날짜보다 이전이면
                if (lastMsgDate.isBefore(currentDate)) {
                    model.addAttribute("currentDate", currentDate.toString());
                } else {
                    model.addAttribute("lastMsgTime", lastMsgTime); // 마지막 메시지 날짜를 그대로 담음
                }
            } else {
                // 마지막 메시지 날짜가 없을 경우 현재 날짜를 담음
                model.addAttribute("currentDate", currentDate.toString());
            }
        }

        return "views/chat/friendChat";
    }



}
