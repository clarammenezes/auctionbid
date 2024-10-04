package com.auction.auction.bid.controller;

import com.auction.auction.bid.interfaces.AuctionServiceI;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionServiceI auctionService;

    @PostMapping
    public Auction createAuction(@RequestBody Auction auction) {
        return auctionService.createAuction(auction);
    }

    @PostMapping("/{auctionId}/close")
    public ResponseEntity<String> closeAuction(@PathVariable String auctionId){
        auctionService.closeAuction(auctionId);
        return ResponseEntity.ok("Auction closed successfully");
    }

    @GetMapping("/startingAt")
    public List<Auction> getAuctionsStartingAt(@RequestParam Date date) {
        return auctionService.getAuctionsStartingAt(date);
    }

    @PostMapping
    public ResponseEntity<String> saveAuction(@RequestBody Auction auction) {
        auctionService.saveAuction(auction);
        return ResponseEntity.ok("Auction saved successfully");
    }

    @PostMapping("/{auctionId}/bid")
    public Auction placeBid(@PathVariable String auctionId, @RequestBody Bid bid) {
        return auctionService.placeBid(auctionId, bid);
    }

    @GetMapping
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }


    @GetMapping(value="auction/{ownerId}")
    public List<Auction> getAuctionsByOwnerId(@PathVariable String ownerId) {
        return auctionService.getAuctionsByOwnerId(ownerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuctionById(@PathVariable String id) {
        auctionService.deleteAuctionById(id);
        return ResponseEntity.ok("Auction deleted successfully");
    }

}
