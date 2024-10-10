package com.auction.auction.bid.model;

public enum AuctionStatus {
    CREATED,    // auction is created but not yet started
    IN_PROCESS, // auction is currently active and accepting bids
    COMPLETED,  // auction has ended and a winner has been determined
    CLOSED      // auction is closed and no more changes are allowed
}
