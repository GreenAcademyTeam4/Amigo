package com.example.amigo_project.controller;

import com.example.amigo_project.dto.chat.RoomDataDTO;
import com.example.amigo_project.repository.model.chat.Emoticon;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.ChatService;
import com.example.amigo_project.service.KakaoApiService;
import com.example.amigo_project.service.NaverApiService;
import com.example.amigo_project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final KakaoApiService kakao;
    private final NaverApiService naver;
    private final UserService userService;
    private final ChatService chatService;

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

    @GetMapping("/test")
    public String test(Model model, HttpSession session){
        User user = userService.findUser(1);
        List<Emoticon>emoticonList = chatService.findEmoticonList();
        RoomDataDTO roomDataDTO = new RoomDataDTO();
        roomDataDTO.setClassRoom("1");
        roomDataDTO.setSchool("분포고등학교");
        roomDataDTO.setGrade("1");
        session.setAttribute("roomData",roomDataDTO);
        session.setAttribute("principal",user);
        // school ID 세션에서 가져오기
        System.out.println(emoticonList);
        model.addAttribute("user",user.getId());
        model.addAttribute("emoticonList",emoticonList);
        return "views/chat/voiceChat";
    }


}
