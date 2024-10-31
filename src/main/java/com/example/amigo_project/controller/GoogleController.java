package com.example.amigo_project.controller;

import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.amigo_project.dto.UserDTO.GoogleDTO;
import com.example.amigo_project.service.GoogleService;
import com.example.amigo_project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleService googleService;
    private final UserService userService;

    @GetMapping("/callback")
    public String googleCallback(@RequestParam("code") String code, HttpSession session) throws Exception {
        String resourceToken = googleService.getGoogleAccessToken(code);
        GoogleDTO googleDTO = googleService.createGoogleUser(resourceToken);
        User principal = (User)googleService.findGoogleUser(googleDTO);
        if (principal != null) {
            session.setAttribute("principal", principal);
            return "views/login/schoolSelect";
        } else {
            return "redirect:/";
        }
    }
}

