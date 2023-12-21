package com.example.auction.market.repository;

import com.example.auction.market.domain.MarketItem;
import com.example.auction.market.domain.MarketItemRtLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketItemRtLogRepository extends JpaRepository<MarketItemRtLog, Integer> {

}
