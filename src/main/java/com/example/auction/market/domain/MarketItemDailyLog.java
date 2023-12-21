package com.example.auction.market.domain;

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
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private MarketItem marketItem;

    private Date date;
    private BigDecimal avgPrice;
    private Integer tradeCount;
}
