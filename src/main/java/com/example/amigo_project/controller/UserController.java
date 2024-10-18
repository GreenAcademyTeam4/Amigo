package com.example.amigo_project.controller;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.UserService;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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



    /**
     * 로그인 
     * TODO 로그인 구현 다시 확인 
     * @param 
     * @return
     */
    @PostMapping("/login")
    public String login(HttpSession session, UserDTO.loginDTO dto){
        
        User principal = userService.findUserByIdAndPassword(dto);
        if(principal != null){
            session.setAttribute("principal", principal);
            return "views/login/schoolSelect";
        } else{
            return "redirect:/";
        }
       
    }

    
    

    /**
     * 회원 가입 페이지 호출 메서드
     * @return
     */
    @GetMapping("/join")
    public String joinForm() {
        return "views/login/join";
    }


    
    /**
     * 중복확인 , 회원가입 페이지 에서 사용
     * @param dto
     * @return
     */
    @PostMapping("/checkUserId")
    public ResponseEntity<Map<String, String>> checkUserId(@RequestBody UserDTO.joinDTO dto) {
        Map<String, String > repetitionResult = userService.checkFieldRepetition(dto);
        return ResponseEntity.ok(repetitionResult);
    }

//    @PostMapping("/requestAuth")
//    public ResponseEntity<UserDTO> requestAuth(@RequestBody UserDTO userDTO) {
//
//    }
    /**
     * 회원가입 
     * @param dto
     * @return
     */
    @PostMapping("/join")
    public String joinUser(@ModelAttribute UserDTO.joinDTO dto) {
        int result = userService.joinUser(dto);
        if (result > 0) {
            return "views/login/login";
        } else {
            return "views/login/login";  
        }

    }
}
