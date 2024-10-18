package com.example.amigo_project.controller;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.service.UserService;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/join")
    public String joinForm() {
        return "views/login/join";
    }

    @PostMapping("/checkUserId")
    public ResponseEntity<Map<String, String>> checkUserId(@RequestBody UserDTO.joinDTO dto) {

        Map<String, String > repetitionResult = userService.checkFieldRepetition(dto);
        return ResponseEntity.ok(repetitionResult);
    }
  
    @PostMapping("/join")
    public String joinUser(@ModelAttribute UserDTO.joinDTO dto) {
        int result = userService.joinUser(dto);
        System.out.println("@@@@@@@@@컨트롤까지 도달:" + result);

        if (result > 0) {
            return "views/login/login";
        } else {
            System.out.println("실패애애애ㅐ애애ㅐㅇ");
            return "views/login/login";  // 실패 시 다시 회원가입 페이지로 이동
        }

    }

}
