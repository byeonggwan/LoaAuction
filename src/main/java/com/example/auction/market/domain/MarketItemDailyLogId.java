package com.example.auction.market.domain;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Date;
import lombok.Getter;

@Getter
public class MarketItemDailyLogId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "item_id")
    private MarketItem marketItem;

    private Date date;

    public MarketItemDailyLogId() {

    }

    public MarketItemDailyLogId(MarketItem marketItem, Date date) {
        this.marketItem = marketItem;
        this.date = date;
    }
}
