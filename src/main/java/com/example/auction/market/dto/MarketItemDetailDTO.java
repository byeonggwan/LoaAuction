package com.example.auction.market.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class MarketItemDetailDTO {
    private Integer id;
    private String name;
    private String grade;
    private String icon;
    private List<MarketItemDailyLogDTO> dailyLogs;

    public MarketItemDetailDTO() {
        this.id = null;
        this.name = "";
        this.grade = "";
        this.icon = "";
        this.dailyLogs = new ArrayList<>();
    }
    public MarketItemDetailDTO(Integer id, String name, String grade, String icon, List<MarketItemDailyLogDTO> dailyLogs) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.icon = icon;
        this.dailyLogs = dailyLogs;
    }
}
