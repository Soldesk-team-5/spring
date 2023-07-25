package com.soldesk.TeamProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class mainController {

    @GetMapping("/")
    public String start_page(@SessionAttribute(name = "nickname", required = false) String nickname, Model model) {

        if (nickname == null){
            // 로그인 X
        } else {
            // 로그인
            model.addAttribute("nickname", nickname);
        }

        return "index"; // 탬플릿/index.mustache -> 브라우저 전송
    }

    @GetMapping("/goHome")
    public String goHome() {

        return "index"; // 탬플릿/index.mustache -> 브라우저 전송
    }

}
