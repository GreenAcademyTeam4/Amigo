package com.example.amigo_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.amigo_project.dto.UserDTO.GoogleDTO;
import com.example.amigo_project.repository.model.User;
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
    public String googleCallback(@RequestParam("code") String code, HttpSession session, Model model) throws Exception {
        String resourceToken = googleService.getGoogleAccessToken(code);
        GoogleDTO googleDTO = googleService.createGoogleUser(resourceToken);
        User principal = googleService.findGoogleUser(googleDTO);
        if (principal != null) {
            session.setAttribute("principal", principal);
            userService.updateOnline(principal.getId());
            if(principal.getProfile() != null) {
                String profile = principal.base64Encoding(principal.getProfile());
                session.setAttribute("profile",profile);
            }
            return "views/login/socialInfo";
        } else {
            return "redirect:/";
        }
    }
}

