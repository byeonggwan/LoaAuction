package com.example.auction;

import com.example.auction.market.service.MarketApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MarketApiService marketApiService;

    @GetMapping("/")
    public String index() {
        marketApiService.insertItemsDailyLog().subscribe();
        return "index";
    }
}
