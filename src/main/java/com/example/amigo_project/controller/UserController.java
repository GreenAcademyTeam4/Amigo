package com.example.amigo_project.controller;

import com.example.amigo_project.dto.SchoolDTO;
import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.model.School;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final HttpSession session;
    private final UserService userService;
    private final WebClient webClient;
    private final ScreenController screenController;

    /**
     * 로그인
     * TODO 로그인 구현 다시 확인
     *
     * @param
     * @return
     */

    @PostMapping("/login")
    public String login(HttpSession session, UserDTO.loginDTO dto,Model model) {
        User principal = userService.findUserById(dto);

        if (principal != null) {
            System.out.println(dto);
            System.out.println(principal);
            session.setAttribute("principal", principal);
            if (principal.getNickname() != null) {
                userService.updateOnline(principal.getId());
                if(principal.getProfile() != null) {
                    String profile = principal.base64Encoding(principal.getProfile());
                    session.setAttribute("profile",profile);
                }
                return "redirect:/";
            }

            return "views/login/schoolSelect";
        }

        return "views/login/login";
    }

    /**
     * 로그아웃
     * @return
     */
    @GetMapping("/logout")
    public String logoutHandler() {
        User user = (User)session.getAttribute("principal");
        userService.updateOffline(user.getId());
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 회원 가입 페이지 호출 메서드
     */
    @GetMapping("/join")
    public String joinForm() {
        return "views/login/join";
    }

    /**
     * 중복 확인 - 회원가입 페이지에서 사용
     */
    @PostMapping("/checkUserId")
    public ResponseEntity<Map<String, String>> checkUserId(@RequestBody UserDTO.joinDTO dto) {
        Map<String, String> repetitionResult = userService.checkFieldRepetition(dto);
        return ResponseEntity.ok(repetitionResult);
    }

    @PostMapping("/checkUsernickname")
    public ResponseEntity<Map<String, String>> checkUserNickName(@RequestBody UserDTO.infoDTO dto) {
        System.out.println("SDFAFSADFSAFSAFD");
        Map<String, String> repetitionResult = userService.checkNickNameRepetition(dto);
        return ResponseEntity.ok(repetitionResult);

    }

    /**
     * 회원가입
     * @param dto
     * @return
     */
    @PostMapping("/join")
    public String joinUser(@ModelAttribute UserDTO.joinDTO dto) {
        int result = userService.joinUser(dto);
        return (result > 0) ? "views/login/login" : "views/login/login";
    }

    /**
     * 학교 데이터 가져오기
     */
    @GetMapping("/schoolData")
    @ResponseBody
    public Mono<List<School>> schoolData(@RequestParam(name = "region") String region, @RequestParam(name = "name") String name) {
        final String KEY = "09bbdab31c0d461c99f7216c700127cd";
        final String Type = "json";
        final Integer pindex = 1;
        final Integer pSize = 1000;
        List<School> schoolList = new ArrayList<>();
        Mono<JsonNode> response = webClient.get().uri(uribuilder -> uribuilder.path("/hub/schoolInfo")
                        .queryParam("KEY", KEY)
                        .queryParam("Type", Type)
                        .queryParam("pindex", pindex)
                        .queryParam("pSize", pSize)
                        .queryParam("ATPT_OFCDC_SC_CODE", region)
                        .queryParam("SCHUL_NM", name)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .onErrorResume(e -> {
                    System.out.println(e.getMessage());
                    return Mono.error(e);
                });

        // JSON 데이터에서 필요한 필드 추출
        return response.map(jsonNode -> {
            JsonNode schoolInfoArray = jsonNode.get("schoolInfo");
            if (schoolInfoArray != null && schoolInfoArray.isArray()) {
                JsonNode rows = schoolInfoArray.get(1).get("row");  // 두 번째 객체에서 "row" 배열 접근
                if (rows != null && rows.isArray()) {
                    for (JsonNode row : rows) {
                        String schoolName = row.get("SCHUL_NM").asText();
                        String code = row.get("SD_SCHUL_CODE").asText();
                        String regions = row.get("LCTN_SC_NM").asText();
                        int id = Integer.parseInt(code);
                        School school = School.builder().id(id).name(schoolName).region(regions).build();
                        schoolList.add(school);  // 리스트에 추가
                    }
                }
            }
            return schoolList;  // 학교 이름 리스트 반환
        });
    }

    /**
     * 테스트 페이지 호출
     */
    @GetMapping("/test")
    public String test(Model model) {
        List<SchoolDTO> schoolList = Arrays.asList(
                new SchoolDTO("B10", "서울"),
                new SchoolDTO("C10", "부산"),
                new SchoolDTO("D10", "대구"),
                new SchoolDTO("E10", "인천"),
                new SchoolDTO("F10", "광주"),
                new SchoolDTO("G10", "대전"),
                new SchoolDTO("H10", "울산"),
                new SchoolDTO("I10", "세종"),
                new SchoolDTO("J10", "경기도"),
                new SchoolDTO("K10", "강원도"),
                new SchoolDTO("M10", "충청북도"),
                new SchoolDTO("N10", "충청남도"),
                new SchoolDTO("P10", "전북"),
                new SchoolDTO("Q10", "전남"),
                new SchoolDTO("R10", "경북"),
                new SchoolDTO("S10", "경남"),
                new SchoolDTO("T10", "제주"),
                new SchoolDTO("V10", "재외한국")
        );

        model.addAttribute("schoolList", schoolList);
        return "views/test";
    }
      
    /**
     * 사용자 정보 업데이트
     */
    @PostMapping("/addInformation")
    public String updateInfo(HttpSession session, @ModelAttribute UserDTO.infoDTO dto,Model model) throws IOException {
        User principal = (User)session.getAttribute("principal");
        dto.setId(principal.getId());
        if(principal.getGender() != null){
            dto.setGender(principal.getGender());
            dto.setName(principal.getName());
            dto.setBirth(principal.getBirth());
            dto.setPhoneNumber(principal.getPhoneNumber());
            userService.updateInfo(dto);
        } else {
            userService.updateInfo(dto);
        }
        if(!userService.existsSchool(dto)) {
            userService.createSchool(dto);
        }
        userService.createUserSchool(dto);
        List<School>schoolList = userService.findUserSchoolList(dto.getId());
        User user = userService.findUser(principal.getId());
        if(user.getGender().equals("male")) {
            byte[]profile = user.convertFileToBytes("static/image/avator/male_head.png");
            user.setProfile(profile);
            userService.insertUserProfile(user);
        } else {
            byte[]profile = user.convertFileToBytes("static/image/avator/female_head.png");
            user.setProfile(profile);
            userService.insertUserProfile(user);
        }
        userService.updateOnline(user.getId());
        String profile = user.base64Encoding(user.getProfile());
        // 유저 정보 업데이트
        session.setAttribute("principal",user);
        // 유저의 프로필을 base64로 인코딩하여 세션에 등록
        session.setAttribute("profile",profile);
        // 메인페이지 들어갈때는 첫번째에 담긴 id를 넣어줌
        session.setAttribute("schoolId",schoolList.get(0).getId());
        model.addAttribute("schoolList",schoolList);
        return "redirect:/";
    }
}
