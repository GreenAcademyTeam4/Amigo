package com.example.amigo_project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.amigo_project.service.KakaoApiService;
import com.example.amigo_project.service.NaverApiService;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class MainController {

    private final KakaoApiService kakao;
    private final NaverApiService naver;


    @GetMapping("/")
    public String firstPage() {

        return "first";
    }

    @GetMapping("/login")
    public String logincontroller(Model model) {
        String kakaolocation ="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+kakao.getKakaoApiKey() +"&redirect_uri="+kakao.getKakaoRedirectUri();
        model.addAttribute("kakaolocation", kakaolocation);
        
        String naverlocation ="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="+naver.getNaverClientId()+"&client_secret="+naver
        .getNaverSecret()+"&redirect_uri="+ naver.getNaverRedirectUri();
        model.addAttribute("naverlocation", naverlocation);
        return "views/login/login";
    }
    
}
