package com.example.auction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final LostarkApiService lostarkApiService;

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
