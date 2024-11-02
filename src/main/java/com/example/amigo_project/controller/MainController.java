package com.example.amigo_project.controller;

import com.example.amigo_project.dto.chat.RoomDataDTO;
import com.example.amigo_project.repository.interfaces.UserRepository;
import com.example.amigo_project.repository.model.School;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.chat.Emoticon;
import com.example.amigo_project.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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
public String firstPage(Model model, HttpSession session) {
    // 필요한 변수를 모델에 추가
    User user = (User)session.getAttribute("principal");
    model.addAttribute("content", "Welcome to the first page!");
    List<User>onlineFriends = userRepository.findOnlineFriends(user.getId());
    List<User>offlineFriends = userRepository.findOfflineFriends(user.getId());
    List<School> schoolList = userService.findUserSchoolList(user.getId());
    session.setAttribute("schoolId",schoolList.get(0).getId());
    model.addAttribute("userSchool",schoolList);
    model.addAttribute("onlineFriendList", onlineFriends);
    model.addAttribute("offlineFriendList", offlineFriends);
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
        List<School>schoolList = userRepository.findUserSchool(user.getId());
        session.setAttribute("schoolId",schoolList.get(0).getId());
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
        List<School>schoolList = userRepository.findUserSchool(user.getId());
        session.setAttribute("schoolId",schoolList.get(0).getId());
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
        roomDataDTO.setClassRoom(1);
        roomDataDTO.setSchoolId(1);
        roomDataDTO.setGrade(1);
        session.setAttribute("roomData",roomDataDTO);
        session.setAttribute("principal",user);
        // schoolId ID 세션에서 가져오기
        System.out.println(emoticonList);
        model.addAttribute("onlineFriendList", onlineFriends);
        model.addAttribute("friendId", friendId);
        model.addAttribute("user",user.getId());
        model.addAttribute("emoticonList",emoticonList);
        return "views/chat/voiceChat";
    }

    @GetMapping("/enter")
    public String enterSchool(Model model,HttpSession session) {
        int schoolId = (Integer)session.getAttribute("schoolId");
        School school = userService.findSchoolData(schoolId);
        // 학년 목록 초기화
        List<Integer>grades = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            grades.add(i,i+1);
        }
        // 반 목록 초기화
        List<Integer>classes = new ArrayList<>(10);
        for(int i = 0; i < 10; i++) {
            classes.add(i,i+1);
        }
        model.addAttribute("schoolName",school.getName());
        model.addAttribute("grades",grades);
        model.addAttribute("classes",classes);
        return "views/classroom/schoolHall";
    }

    @PostMapping("/enterClass")
    public String enterClassroom(@RequestBody Map<String, Integer> params, Model model,HttpSession session) {
        User user = (User)session.getAttribute("principal");
        List<Emoticon>emoticonList = chatService.findEmoticonList();
        System.out.println("들어오는중 !!!!!");
        Integer grade = params.get("grade");
        Integer classSelect = params.get("class");
        Integer schoolId = (Integer)session.getAttribute("schoolId");
        RoomDataDTO roomDataDTO = new RoomDataDTO();
        roomDataDTO.setClassRoom(classSelect);
        roomDataDTO.setGrade(grade);
        roomDataDTO.setSchoolId(schoolId);
        session.setAttribute("roomData",roomDataDTO);
        model.addAttribute("user",user.getId());
        model.addAttribute("emoticonList",emoticonList);
        return "views/classroom/classroom2";
    }
}
