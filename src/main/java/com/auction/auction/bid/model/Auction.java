package com.auction.auction.bid.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Document(collection = "auction")
public class Auction {
    @Id
    private String id;
    private String title;
    private String description;
    private String owner;
    private double startingBid;
    private double currentBid;
    private List<Bid> bids;
    private boolean isClosed;
    private Date startDate;
    private Date endDate;
    private double bidAmount;
    private AuctionStatus status;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Auction() {
    }

    public Auction(String id, String title, String description, String owner, double startingBid, double currentBid, List<Bid> bids, Date startDate, Date endDate, double bidAmount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.startingBid = startingBid;
        this.currentBid = currentBid;
        this.bids = bids;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bidAmount = bidAmount;

    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

}
