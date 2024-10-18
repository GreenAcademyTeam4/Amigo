package com.example.amigo_project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 스크린 변경 컨트롤러
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/main")
public class ScreenController {

    private final HttpSession session;
    /**
     * 게시판으로 화면 전환
     * @return
     */
    @GetMapping("/post")
    public String boardViewHandler(Model model) {
        int schoolId = (Integer)session.getAttribute("schoolId");
        // TODO schoolId로 해당하는 학교의 게시판리스트를 가져와서 Model에 넣기
        return "views/post";
    }

    /**
     * 학교로 들어가기
     * @return
     */
    @GetMapping("/school")
    public String schoolViewHandler() {
        return "views/school";
    }

    /**
     * 현재 학교 상태 바꾸기
     * @param schoolId
     * @return
     */
    @GetMapping("/changeSchool")
    public String changeSchoolViewHandler(@RequestParam(name="id")int schoolId) {
        session.setAttribute("schoolId", schoolId);
        return "redirect:/main/post";
    }

    @PostMapping("/classroom")
    public String classroomViewHandler(@RequestParam(name="grade")int grade,
                                       @RequestParam(name="class")int classroom,
                                       Model model) {

        int schoolId = (Integer)session.getAttribute("schoolId");
        model.addAttribute("schoolId", schoolId);
        model.addAttribute("grade", grade);
        model.addAttribute("class", classroom);
        return "views/classroom";
    }
}
