package com.soldesk.TeamProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    @GetMapping("/")
    public String go_main(Model model) {
        model.addAttribute("username", "성혁");
        return "index"; // 탬플릿/index.mustache -> 브라우저 전송
    }

    @GetMapping("/DBt")
    public String db(Model model) {
        model.addAttribute("username", "성혁");
        return "DBtest";
    }

    @GetMapping("/recommend")
    public String go_recommend() {
        return "recommend";
    }
}
