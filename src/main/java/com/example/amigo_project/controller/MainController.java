package com.example.amigo_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String logincontroller() {

        return "views/login/login";
    }
}
