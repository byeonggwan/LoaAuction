package com.example.auction.auction.repository;

import com.example.auction.auction.domain.AuctionItem;
import com.example.auction.auction.domain.AuctionItemRtLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionItemRtLogRepository extends JpaRepository<AuctionItemRtLog, Integer> {

}
