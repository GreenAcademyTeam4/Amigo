package com.example.amigo_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.amigo_project.dto.UserDTO.KakaoDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.KakaoApiService;
import com.example.amigo_project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoApiService kakaoApiService;
    private final UserService userService;

    @GetMapping("/callback")
    public String kakaoCallResource(@RequestParam("code") String code, HttpSession session) throws Exception {
        
        String resourceToken = kakaoApiService.getKakaoAccessToken(code);
        KakaoDTO kakaoDTO = kakaoApiService.createKakaoUser(resourceToken);
        User principal = kakaoApiService.findKakaoUser(kakaoDTO);
        if (principal != null) {
            session.setAttribute("principal", principal);
            if (principal.getNickname() != null) {
                return "redirect:/";
            }
            return "views/login/socialInfo";
        } else {
            return "redirect:/";
        }

    }
}
