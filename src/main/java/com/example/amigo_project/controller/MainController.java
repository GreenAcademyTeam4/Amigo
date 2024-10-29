package com.example.amigo_project.controller;

import java.net.URLEncoder;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.amigo_project.service.GoogleService;
import com.example.amigo_project.service.KakaoApiService;
import com.example.amigo_project.service.NaverApiService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final KakaoApiService kakao;
    private final NaverApiService naver;
    private final GoogleService google;
  


    @GetMapping("/")
public String firstPage(Model model) {
    // 필요한 변수를 모델에 추가
    model.addAttribute("content", "Welcome to the first page!");
    model.addAttribute("msg", "Hello, this is a message.");

    return "index";
}

  @GetMapping("/login")
public String logincontroller(Model model, HttpSession session) throws Exception {

    // 카카오 로그인 URL 생성
    String kakaolocation = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
            + kakao.getKakaoApiKey() + "&redirect_uri=" + kakao.getKakaoRedirectUri();

    model.addAttribute("kakaolocation", kakaolocation);
    // 네이버 로그인 URL 생성
    // 고유한 state 값 생성 (UUID 사용)
    String state = UUID.randomUUID().toString();
    String encodedState = URLEncoder.encode(state, "UTF-8");
    session.setAttribute("oauthState", state); // 원본 state 값을 세션에 저장

    String naverlocation = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
    + "&client_id=" + naver.getNaverClientId()
    + "&state=" + encodedState
    + "&redirect_uri=" + URLEncoder.encode(naver.getNaverRedirectUri(), "UTF-8");
model.addAttribute("naverlocation", naverlocation);

String googleLocation = "https://accounts.google.com/o/oauth2/auth?client_id=" +google.getCLIENT_ID()
+ "&redirect_uri=" + URLEncoder.encode(google.getREDIRECT_URI(), "UTF-8")
+ "&response_type=code"
+ "&scope=email%20profile";
model.addAttribute("googleLocation", googleLocation);

return "views/login/login";


   
}

    @GetMapping("/test")
    public String test(HttpSession session, @RequestParam(name="grade") int grade,
                       @RequestParam(name="class") int classNum, Model model){
        // school ID 세션에서 가져오기
        int schoolId = (Integer) session.getAttribute("schoolId");


        return "views/classroom/classroom";
    }


}
