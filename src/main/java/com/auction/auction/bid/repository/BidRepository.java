package com.auction.auction.bid.repository;

import com.auction.auction.bid.model.Bid;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BidRepository extends MongoRepository<Bid, String> {
}
