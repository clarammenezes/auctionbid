package com.auction.auction.bid.exception;

public class AuctionNotFoundException extends RuntimeException {
    public AuctionNotFoundException(String id) {
        super("Auction not found with id: " + id);
    }
}
