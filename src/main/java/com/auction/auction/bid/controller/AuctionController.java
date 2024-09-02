package com.auction.auction.bid.controller;

import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public Auction createAuction(@RequestBody Auction auction) {
        return auctionService.createAuction(auction);
    }

    @PostMapping(value = "/sendMessage", consumes = "text/plain")
    public void sendMessage(@RequestBody String message) {
        auctionService.sendMessage(message);
    }

//    @PostMapping
//    public Auction saveOrUpdateAuction(@RequestBody Auction auction){
//        return auctionService.saveOrUpdateAuction(auction);
//    }

    @GetMapping
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

//    @GetMapping
//    public List<Auction> getAuctionsByTitle(@RequestParam String title) {
//        return auctionService.getAuctionsByTitle(title);
//    }
//
//    @GetMapping
//    public List<Auction> getAuctionsBySeller(@RequestParam String seller) {
//        return auctionService.getAuctionsBySeller(seller);
//    }
//
//    @GetMapping
//    public Auction getAuctionsByStartingBid(@RequestParam double startingBid) {
//        return auctionService.getAuctionsByStartingBid(startingBid);
//    }


}
