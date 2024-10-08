package com.auction.auction.bid.interfaces;

import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.Bid;

import java.util.Date;
import java.util.List;

public interface AuctionServiceI {
    Auction saveAuction(Auction auction);
    Auction getAuctionById(String id);
    List<Auction> getAllAuctions();
    void deleteAuctionById(String id);
    List<Auction> getAuctionsByOwnerId(String ownerId);
    List<Auction> getAuctionsStartingAt(Date startDate);
    Auction placeBid(String auctionId, Bid bid);
    Auction createAuction(Auction auction);
    void closeAuction(String auctionId);
}
