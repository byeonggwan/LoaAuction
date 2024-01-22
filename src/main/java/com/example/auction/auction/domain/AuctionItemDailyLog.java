package com.example.auction.auction.domain;

import jakarta.persistence.EmbeddedId;
import java.math.BigDecimal;

public class AuctionItemDailyLog {
    @EmbeddedId
    private AuctionItemDailyLogId id;

    private BigDecimal avgPrice;
}
