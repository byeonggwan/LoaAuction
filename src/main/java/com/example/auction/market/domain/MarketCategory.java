package com.example.auction.market.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class MarketCategory {
    @Id
    private Integer id;

    private String name;

    private Integer totalCount;

    public MarketCategory(Integer id, String name, Integer totalCount) {
        this.id = id;
        this.name = name;
        this.totalCount = totalCount;
    }

    public MarketCategory() {

    }

    public void updateTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
