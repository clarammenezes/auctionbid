package com.auction.auction.bid.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Document(collection = "auction")
public class Auction {
    @Id
    private String id;
    private String title;
    private String description;
    private String seller;
    private double startingBid;
    private double currentBid;
    private List<Bid> bids;
    private boolean isClosed;

    public Auction(String id, String title, String description, String seller, double startingBid, double currentBid, List<Bid> bids) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.seller = seller;
        this.startingBid = startingBid;
        this.currentBid = currentBid;
        this.bids = bids;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
