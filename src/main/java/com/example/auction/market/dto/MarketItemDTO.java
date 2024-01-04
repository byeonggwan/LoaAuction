package com.example.auction.market.dto;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class MarketItemDTO {
    private Integer id;
    private String name;
    private String grade;
    private String icon;
    private BigDecimal avgPrice;
    private Integer tradeCount;

    public MarketItemDTO(Integer id, String name, String grade, String icon, BigDecimal avgPrice, Integer tradeCount) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.icon = icon;
        this.avgPrice = avgPrice;
        this.tradeCount = tradeCount;
    }
}
