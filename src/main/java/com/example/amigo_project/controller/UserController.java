package com.example.amigo_project.controller;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/requestAuth")
    public ResponseEntity<UserDTO> requestAuth(@RequestBody UserDTO userDTO) {
        return null;
    }
}
