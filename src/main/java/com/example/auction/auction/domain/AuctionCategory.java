package com.example.auction.auction.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class AuctionCategory {
    @Id
    private Integer id;

    private String name;

    public AuctionCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public AuctionCategory() {

    }
}
