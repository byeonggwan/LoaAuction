package com.example.auction.market.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MarketCategory {
    @Id
    private Integer id;

    private String name;

    public MarketCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public MarketCategory() {

    }
}
