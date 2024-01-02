package com.sparta.jamrello.domain.ui;

import com.sparta.jamrello.domain.member.dto.LoginRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jamrello")
public class HomeController {

    @GetMapping("/main")
    public String mainPage() {
        return "test";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

}
