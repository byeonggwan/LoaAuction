package com.example.auction.auction.repository;

import com.example.auction.auction.domain.AuctionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionCategoryRepository extends JpaRepository<AuctionCategory, Integer> {

}
