package com.auction.auction.bid.controller;

import com.auction.auction.bid.interfaces.BidIServiceI;
import com.auction.auction.bid.model.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidIServiceI bidService;


    @PostMapping("/{auctionId}/saveBid")
    public ResponseEntity<Bid> saveBid(@PathVariable String auctionId, @RequestBody Bid bid) {
        Bid savedBid = bidService.saveBid(auctionId, bid);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBid);
    }


    @PostMapping("/place/{auctionId}")
    public ResponseEntity<Bid> placeBid(@PathVariable String auctionId, @RequestBody Bid bid) {
        Bid placedBid = bidService.placeBid(auctionId, bid);
        return ResponseEntity.status(HttpStatus.CREATED).body(placedBid);
    }
}
