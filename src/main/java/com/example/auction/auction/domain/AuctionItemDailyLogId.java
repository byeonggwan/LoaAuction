package com.example.auction.auction.domain;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Date;
import lombok.Getter;

@Getter
public class AuctionItemDailyLogId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private AuctionItem item;

    private Date date;
}
