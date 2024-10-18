package com.example.amigo_project.controller;

import com.example.amigo_project.dto.SchoolDTO;
import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final WebClient webClient;

    private final UserService userService;


    @GetMapping("/join")
    public String joinForm() {
        return "views/login/join";
    }

    @PostMapping("/checkUserId")
    public ResponseEntity<Map<String, String>> checkUserId(@RequestBody UserDTO.joinDTO dto) {

        Map<String, String> repetitionResult = userService.checkFieldRepetition(dto);
        return ResponseEntity.ok(repetitionResult);
    }

    @PostMapping("/join")
    public String joinUser(@ModelAttribute UserDTO.joinDTO dto) {
        int result = userService.joinUser(dto);
        System.out.println("@@@@@@@@@컨트롤까지 도달:" + result);

        if (result > 0) {
            return "views/login/login";
        } else {
            System.out.println("실패애애애ㅐ애애ㅐㅇ");
            return "views/login/login";  // 실패 시 다시 회원가입 페이지로 이동
        }

    }

    @GetMapping("/schoolData")
    @ResponseBody
    public Mono<List<String>> schoolData(@RequestParam(name = "region") String region,
                                         @RequestParam(name = "name") String name) {
        final String KEY = "09bbdab31c0d461c99f7216c700127cd";
        final String Type = "json";
        final Integer pindex = 1;
        final Integer pSize = 1000;
        System.out.println("들어옴!!!!!!" + region);
        System.out.println("들어옴!!!!!!" + name);
        List<String> schoolList = new ArrayList<>();
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
            // "schoolInfo" 배열 내 "row" 필드 탐색
            JsonNode schoolInfoArray = jsonNode.get("schoolInfo");

            if (schoolInfoArray != null && schoolInfoArray.isArray()) {
                JsonNode rows = schoolInfoArray.get(1).get("row");  // 두 번째 객체에서 "row" 배열 접근
                if (rows != null && rows.isArray()) {
                    for (JsonNode row : rows) {
                        // 각 학교의 "SCHUL_NM" 필드 추출
                        String schoolName = row.get("SCHUL_NM").asText();
                        System.out.println(schoolName);
                        schoolList.add(schoolName);  // 리스트에 추가
                    }
                }
            }

            return schoolList;  // 학교 이름 리스트 반환
        });
    }

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

        model.addAttribute("schoolList", schoolList);  // schoolList를 모델에 추가하여 뷰에 전달
        return "views/test";  // test.mustache 또는 test.html로 전달
    }
}
