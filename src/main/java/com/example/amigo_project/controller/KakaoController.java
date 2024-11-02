package com.example.amigo_project.controller;

import com.example.amigo_project.repository.model.School;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.amigo_project.dto.UserDTO.KakaoDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.KakaoApiService;
import com.example.amigo_project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoApiService kakaoApiService;
    private final UserService userService;

    @GetMapping("/callback")
    public String kakaoCallResource(@RequestParam("code") String code, HttpSession session, Model model) throws Exception {
        
        String resourceToken = kakaoApiService.getKakaoAccessToken(code);
        KakaoDTO kakaoDTO = kakaoApiService.createKakaoUser(resourceToken);
        User principal = kakaoApiService.findKakaoUser(kakaoDTO);
        if (principal != null) {
            session.setAttribute("principal", principal);
            if (principal.getNickname() != null) {
                userService.updateOnline(principal.getId());
                List<School> schoolList = userService.findUserSchoolList(principal.getId());
                model.addAttribute("schoolList",schoolList);
                String profile = principal.base64Encoding(principal.getProfile());
                session.setAttribute("profile",profile);
                session.setAttribute("schoolId",schoolList.get(0).getId());
                return "redirect:/";
            }
            return "views/login/socialInfo";
        } else {
            return "redirect:/";
        }

    }
}
