package com.example.amigo_project.controller;

import com.example.amigo_project.dto.ChatRoomDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public String firstPage() {

        return "first";
    }

    @GetMapping("/login")
    public String logincontroller() {
        return "views/login/login";
    }

    @GetMapping("/test")
    public String test(HttpSession session, Model model) throws JsonProcessingException {


        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setSchool("삼정고등학교");
        dto.setGrade(3);
        dto.setClassroom(10);
        // 세션에 방 번호 등록
        session.setAttribute("chatRoomDTO",dto);


        return "views/classroom/classroom";
    }


}
