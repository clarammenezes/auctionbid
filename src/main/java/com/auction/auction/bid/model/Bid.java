package com.auction.auction.bid.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "bid")
public class Bid {
    @Id
    private String id;
    private String auctionId; //username
    private String bidder;
    private double amount;

    public Bid(String id, String auctionId, String bidder, double amount) {
        this.id = id;
        this.auctionId = auctionId;
        this.bidder = bidder;
        this.amount = amount;
    }
}
