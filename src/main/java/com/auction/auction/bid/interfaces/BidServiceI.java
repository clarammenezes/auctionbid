package com.auction.auction.bid.interfaces;

import com.auction.auction.bid.model.Bid;


public interface BidServiceI {
    Bid placeBid(String auctionId, Bid bid);
}
