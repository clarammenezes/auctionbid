package com.auction.auction.bid.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Auction(String id, String title, String description, String seller, double startingBid, double currentBid) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.seller = seller;
        this.startingBid = startingBid;
        this.currentBid = currentBid;
    }
}
