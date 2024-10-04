package com.auction.auction.bid.interfaces;

import com.auction.auction.bid.model.Bid;


public interface BidIServiceI {
    Bid saveBid(String auctionId, Bid bid);
    Bid placeBid(String auctionId, Bid bid);
}
