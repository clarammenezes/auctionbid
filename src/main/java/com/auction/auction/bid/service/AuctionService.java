package com.auction.auction.bid.service;

import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionService {
    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public Auction saveOrUpdateAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public Auction createAuction(Auction auction){
        return auctionRepository.save(auction);
    }

    public void deleteAllAuctions() {
        auctionRepository.deleteAll();
    }

    public Auction getAuctionById(String id) {
        return auctionRepository.findById(id).get();
    }

    public Auction getAuctionByDescription(String description) {
        return auctionRepository.findByDescription(description).get(0);
    }

    public Auction getAuctionsByStartingBid(double startingBid) {
        return auctionRepository.findByStartingBid(startingBid).get(0);
    }

    public List<Auction> getAuctionByCurrentBidGreaterThan(double currentBid) {
        return auctionRepository.findByCurrentBidGreaterThan(currentBid);
    }

    public List<Auction> getAuctionByCurrentBidLessThan(double currentBid) {
        return auctionRepository.findByCurrentBidLessThan(currentBid);
    }

    public List<Auction> getAuctionByCurrentBidBetween(double min, double max) {
        return auctionRepository.findByCurrentBidBetween(min, max);
    }

    public List<Auction> getAuctionByTitleAndCurrentBidGreaterThan(String title, double currentBid) {
        return auctionRepository.findByTitleAndCurrentBidGreaterThan(title, currentBid);
    }

    public List<Auction> getAuctionByTitleAndCurrentBidLessThan(String title, double currentBid) {
        return auctionRepository.findByTitleAndCurrentBidLessThan(title, currentBid);
    }

    public void deleteAuctionById(String id) {
        auctionRepository.deleteById(id);
    }

    public List<Auction> getAuctionsByTitle(String title) {
        return auctionRepository.findByTitle(title);
    }

    public List<Auction> getAuctionsBySeller(String seller) {
        return auctionRepository.findBySeller(seller);
    }

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public List<Auction> getAuctionsByTitleAndBidRange(String title, double minBid, double maxBid) {
        return auctionRepository.findByTitleAndCurrentBidBetween(title, minBid, maxBid);
    }

    public void sendMessage(String message) {
        this.kafkaTemplate.send("auctions", message);
    }


}
