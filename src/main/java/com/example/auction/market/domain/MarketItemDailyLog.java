package com.example.auction.market.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
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
