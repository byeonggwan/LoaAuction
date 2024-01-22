package com.example.auction.auction.repository;

import com.example.auction.auction.domain.AuctionCategory;
import com.example.auction.auction.domain.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem, Integer> {

}