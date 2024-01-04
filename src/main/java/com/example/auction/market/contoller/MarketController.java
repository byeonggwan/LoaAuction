package com.example.auction.market.contoller;

import com.example.auction.market.dto.MarketItemDTO;
import com.example.auction.market.dto.MarketItemDetailDTO;
import com.example.auction.market.service.MarketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("market")
@RequiredArgsConstructor
public class MarketController {
    private final MarketService marketService;

    // TODO: 페이지네이션
    @GetMapping("")
    public String marketIndex(Model model) {
        List<MarketItemDTO> marketItemSummary = marketService.getMarketItemSummary();
        model.addAttribute("marketItems", marketItemSummary);
        return "marketIndex";
    }

    @GetMapping("{itemId}")
    public String marketDetail(@PathVariable Integer itemId, Model model) {
        MarketItemDetailDTO marketItemDetail = marketService.getMarketItemDetail(itemId);
        model.addAttribute("marketItemDetail", marketItemDetail);
        return "marketDetail";
    }
}
