package com.auction.auction.bid.repository;

import com.auction.auction.bid.model.Auction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends MongoRepository<Auction, String> {
    List<Auction> findByOwner(String owner);

    // Custom query to find auctions starting at a specific date and hour
    @Query("{ 'startDate': { $gte: ?0, $lt: ?1 } }")
    List<Auction> findAuctionsStartingAt(Date startDate, Date endDate);

}
