package com.auction.auction.bid.controller;

import com.auction.auction.bid.interfaces.BidServiceI;
import com.auction.auction.bid.model.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidServiceI bidService;

    @PostMapping("/place/{auctionId}")
    public ResponseEntity<Bid> placeBid(@PathVariable String auctionId, @RequestBody Bid bid) {
        Bid placedBid = bidService.placeBid(auctionId, bid);
        return ResponseEntity.status(HttpStatus.CREATED).body(placedBid);
    }
}
