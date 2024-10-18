package com.example.amigo_project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public String logincontroller() {

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
