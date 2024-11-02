package com.example.amigo_project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.amigo_project.dto.UserDTO.NaverDTO;
import com.example.amigo_project.repository.model.School;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.NaverApiService;
import com.example.amigo_project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/naver")
public class NaverController {

    private final NaverApiService naverApiService;
    private final UserService userService;

    @GetMapping("/callback")
  
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpSession session, Model model) throws Exception {

        // 세션에서 저장된 state 값 가져오기
        String sessionState = (String) session.getAttribute("oauthState");

        // 전달된 state 값이 일치하는지 확인 (검증)
        if (sessionState == null || !sessionState.equals(state)) {
            throw new IllegalStateException("Invalid state parameter");
        }

        // 네이버 액세스 토큰 가져오기
        String resourceToken = naverApiService.getNaverAccessToken(code, state);

        // 네이버 사용자 정보 생성
        NaverDTO naverDTO = naverApiService.createNaverUser(resourceToken);

        // 네이버 사용자 찾기 또는 생성
        User principal = naverApiService.findNaverUser(naverDTO);
       
        if (principal != null) {
            session.setAttribute("principal", principal);
            if (principal.getNickname() != null) {
            System.out.println("네이버 로그인 진입");
                userService.updateOnline(principal.getId());
                if(principal.getProfile() != null) {
                    String profile = principal.base64Encoding(principal.getProfile());
                    session.setAttribute("profile",profile);
                }
                String profile = principal.base64Encoding(principal.getProfile());
                session.setAttribute("profile",profile);
                return "redirect:/";
            }
            return "views/login/socialInfo";

        } else {
            return "redirect:/";
        }
    }
}