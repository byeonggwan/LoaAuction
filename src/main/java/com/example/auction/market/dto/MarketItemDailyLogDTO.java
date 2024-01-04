package com.example.auction.market.dto;

import java.math.BigDecimal;
import java.sql.Date;
import lombok.Getter;

@Getter
public class MarketItemDailyLogDTO {
    private Date date;
    private BigDecimal avgPrice;
    private Integer tradeCount;

    public MarketItemDailyLogDTO() {
        this.date = null;
        this.avgPrice = null;
        this.tradeCount = null;
    }
    public MarketItemDailyLogDTO(Date date, BigDecimal avgPrice, Integer tradeCount) {
        this.date = date;
        this.avgPrice = avgPrice;
        this.tradeCount = tradeCount;
    }
}
