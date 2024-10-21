package com.example.amigo_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {
    @GetMapping("/")
    public String firstPage() {

        return "first";
    }

    @GetMapping("/login")
    public String logincontroller() {
        return "views/login/login";
    }
    
}
