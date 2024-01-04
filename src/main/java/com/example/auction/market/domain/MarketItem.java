package com.example.auction.market.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class MarketItem {
    @Id
    private Integer id;

    private String name;
    private String grade;
    private String icon;

    @ManyToOne
    @JoinColumn(name="category_id")
    private MarketCategory category;

    @OneToMany(mappedBy = "id.marketItem", fetch = FetchType.LAZY)
    private List<MarketItemDailyLog> dailyLogs;

    public MarketItem() {

    }

    public MarketItem(Integer id, String name, String grade, String icon, MarketCategory marketCategory) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.icon = icon;
        this.category = marketCategory;
        this.dailyLogs = new ArrayList<>();
    }
}
