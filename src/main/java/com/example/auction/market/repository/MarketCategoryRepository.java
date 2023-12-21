package com.example.auction.market.repository;

import com.example.auction.market.domain.MarketCategory;
import com.example.auction.market.domain.MarketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketCategoryRepository extends JpaRepository<MarketCategory, Integer> {

}
