package com.example.auction.auction.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class AuctionItem {
    @Id
    private Integer id;

    private String name;
    private String grade;
    private String icon;

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID")
    private AuctionCategory category;
}
