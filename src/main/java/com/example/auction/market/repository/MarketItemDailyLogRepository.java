package com.example.auction.market.repository;

import com.example.auction.market.domain.MarketItem;
import com.example.auction.market.domain.MarketItemDailyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketItemDailyLogRepository extends JpaRepository<MarketItemDailyLog, Integer> {

}
