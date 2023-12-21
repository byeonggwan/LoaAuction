package com.example.auction.market.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.Date;

@Entity
public class MarketItemRtLog {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private MarketItem marketItem;

    private Date createdAt;
    private Integer recentPrice;
    private Integer currentMinPrice;
}
