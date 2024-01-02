package com.sparta.jamrello.domain.ui;

import com.sparta.jamrello.domain.member.dto.LoginRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jamrello")
@Slf4j
public class HomeController {

    @GetMapping("/main")
    public String mainPage() {
        return "test";
    }

    @GetMapping("/loginPage")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

}
