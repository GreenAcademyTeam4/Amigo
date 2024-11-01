package com.example.amigo_project.controller;

import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import com.example.amigo_project.repository.interfaces.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.amigo_project.dto.chat.RoomDataDTO;
import com.example.amigo_project.repository.model.Emoticon;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.ChatService;
import com.example.amigo_project.service.GoogleService;
import com.example.amigo_project.service.KakaoApiService;
import com.example.amigo_project.service.NaverApiService;
import com.example.amigo_project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final KakaoApiService kakao;
    private final NaverApiService naver;
    private final GoogleService google;
    private final UserService userService;
    private final ChatService chatService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String firstPage() {
        return "index";
    }
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
    @GetMapping("/test2")
    public String test2(Model model, HttpSession session){
        User user = userService.findUser(1);
        session.setAttribute("principal", user);
        List<User>onlineFriends = userRepository.findOnlineFriends(user.getId());
        List<User>offlineFriends = userRepository.findOfflineFriends(user.getId());
        System.out.println(onlineFriends);
        System.out.println(offlineFriends);
        model.addAttribute("onlineFriendList", onlineFriends);
        model.addAttribute("offlineFriendList", offlineFriends);
        return "index";
    }

    @GetMapping("/test3")
    public String test3(Model model, HttpSession session){
        User user = userService.findUser(2);
        session.setAttribute("principal", user);
        List<User>onlineFriends = userRepository.findOnlineFriends(user.getId());
        List<User>offlineFriends = userRepository.findOfflineFriends(user.getId());
        model.addAttribute("onlineFriendList", onlineFriends);
        model.addAttribute("offlineFriendList", offlineFriends);
        return "index";
    }

    @GetMapping("/test/{id}")
    public String test(@PathVariable(name = "id")int friendId, Model model, HttpSession session){
        System.out.println("아이디 잘 들ㅇ옴 !!!!! : " + friendId);
        User user = (User)session.getAttribute("principal");
        List<User>onlineFriends = userRepository.findOnlineFriends(user.getId());
        List<Emoticon>emoticonList = chatService.findEmoticonList();
        RoomDataDTO roomDataDTO = new RoomDataDTO();
        roomDataDTO.setClassRoom("1");
        roomDataDTO.setSchool("분포고등학교");
        roomDataDTO.setGrade("1");
        session.setAttribute("roomData",roomDataDTO);
        session.setAttribute("principal",user);
        // school ID 세션에서 가져오기
        System.out.println(emoticonList);
        model.addAttribute("onlineFriendList", onlineFriends);
        model.addAttribute("friendId", friendId);
        model.addAttribute("user",user.getId());
        model.addAttribute("emoticonList",emoticonList);
        return "views/chat/voiceChat";
    }
}
