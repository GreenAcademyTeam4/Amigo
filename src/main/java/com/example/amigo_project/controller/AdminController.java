package com.example.amigo_project.controller;

import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class AdminController {

    private final AdminService adminService;


    // 메인 화면
    @GetMapping("/admin")
    public String home() {

        return "views/admins/admin"; // index.mustache 파일을 반환 (임시)
    }

    // 회원 관리 - 유저 관리 페이지
    @GetMapping("/user")
    public String userPage(Model model){

        List<User> userList = adminService.getUserList();
        userList.get(0).setOnlineStatus(true);
        System.out.println(userList.toString());


        model.addAttribute("userList", userList);
        return "views/admins/user"; // 임시
    }

    @GetMapping("/user/detail/{id}")
    public String userDetail(Model model, @PathVariable(name = "id") int id){
        User user = adminService.findById(id);
        System.out.println(user);
        model.addAttribute("user", user);
        return "views/admins/userDetail"; // 임시

    }






    // 게시글 관리

    // 결제 관리

    // 광고 관리

    // 공지 관리

    // 문의 관리

    // 신고 관리

    // 통계



}
