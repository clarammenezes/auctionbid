package com.auction.auction.bid.controller;

import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

//    @PostMapping
//    public Auction createAuction(@RequestBody Auction auction) {
//        return auctionService.createAuction(auction);
//    }

    @PostMapping(value = "/sendMessage", consumes = "text/plain")
    public void sendMessage(@RequestBody String message) {
        auctionService.sendMessage(message);
    }

//    @DeleteMapping(value = "/deleteAll")
//    public ResponseEntity<?> deleteAllAuctions() {
//        auctionService.deleteAllAuctions();
//        return ResponseEntity.ok().build();
//    }

    @GetMapping(value="accounts/{id}")
    public Auction getAuctionById(@PathVariable String id) {
        return auctionService.getAuctionById(id);
    }

    @PostMapping
    public Auction saveOrUpdateAuction(@RequestBody Auction auction){
        return auctionService.saveOrUpdateAuction(auction);
    }

    @PostMapping("/{auctionId}/bid")
    public Auction placeBid(@PathVariable String auctionId, @RequestBody Bid bid) {
        return auctionService.placeBid(auctionId, bid);
    }

    @GetMapping
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

//    @GetMapping(value="auction/{title}")
//    public List<Auction> getAuctionsByTitle(@PathVariable String title) {
//        return auctionService.getAuctionsByTitle(title);
//    }
//
//    @GetMapping(value="auction/{seller}")
//    public List<Auction> getAuctionsBySeller(@PathVariable String seller) {
//        return auctionService.getAuctionsBySeller(seller);
//    }
//
//
//    @GetMapping(value="auction/{startingBid}")
//    public Auction getAuctionsByStartingBid(@PathVariable double startingBid) {
//        return auctionService.getAuctionsByStartingBid(startingBid);
//    }
//

//
//    @GetMapping(value="auction/{description}")
//    public Auction getAuctionByDescription(@PathVariable String description) {
//        return auctionService.getAuctionByDescription(description);
//    }
//
//    @GetMapping(value="auction/{currentBid}")
//    public List<Auction> getAuctionByCurrent(@PathVariable double currentBid) {
//        return auctionService.getAuctionByCurrentBid(currentBid);
//    }
}
