package com.example.auction;

import com.example.auction.auction.service.AuctionApiService;
import com.example.auction.market.service.MarketApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MarketApiService marketApiService;
    private final AuctionApiService auctionApiService;

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
