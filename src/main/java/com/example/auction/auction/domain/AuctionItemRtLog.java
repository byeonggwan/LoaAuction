package com.example.auction.auction.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Getter;

@Entity
@Getter
public class AuctionItemRtLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private AuctionItem item;

    private Integer currentMinPrice;
    private BigDecimal avgPrice;
    private Timestamp createAt;

    public AuctionItemRtLog(AuctionItem item, Integer currentMinPrice, BigDecimal avgPrice) {
        this.item = item;
        this.currentMinPrice = currentMinPrice;
        this.avgPrice = avgPrice;
        this.createAt = new Timestamp(System.currentTimeMillis());
    }

    public AuctionItemRtLog() {
    }
}
