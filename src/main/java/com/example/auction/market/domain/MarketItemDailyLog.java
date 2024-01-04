package com.example.auction.market.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.sql.Date;
import lombok.Getter;

@Entity
@Getter
public class MarketItemDailyLog {
    @EmbeddedId
    private MarketItemDailyLogId id;
    private BigDecimal avgPrice;
    private Integer tradeCount;

    public MarketItemDailyLog() {

    }

    public MarketItemDailyLog(MarketItem marketItem, Date date, BigDecimal avgPrice, Integer tradeCount) {
        this.id = new MarketItemDailyLogId(marketItem, date);
        this.avgPrice = avgPrice;
        this.tradeCount = tradeCount;
    }
}
