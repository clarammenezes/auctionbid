package com.auction.auction.bid.repository;

import com.auction.auction.bid.model.Auction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends MongoRepository<Auction, String> {
    List<Auction> findByTitle(String title);
    List<Auction> findBySeller(String seller);
    List<Auction> findByStartingBid(double startingBid);
    List<Auction> findByCurrentBid(double currentBid);
    List<Auction> findByCurrentBidGreaterThan(double currentBid);
    List<Auction> findByCurrentBidLessThan(double currentBid);
    List<Auction> findByCurrentBidBetween(double min, double max);
    List<Auction> findByDescription(String description);
    List<Auction> findByTitleAndCurrentBidGreaterThan(String title, double currentBid);
    List<Auction> findByTitleAndCurrentBidLessThan(String title, double currentBid);
    List<Auction> findByTitleAndCurrentBidBetween(String title, double min, double max);
    // Custom query to find auctions starting at a specific date and hour
    @Query("{ 'startDate': { $gte: ?0, $lt: ?1 } }")
    List<Auction> findAuctionsStartingAt(Date startDate, Date endDate);




}
