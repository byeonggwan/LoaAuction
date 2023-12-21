package com.example.auction.market.repository;

import com.example.auction.market.domain.MarketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketItemRepository extends JpaRepository<MarketItem, Integer> {

}
