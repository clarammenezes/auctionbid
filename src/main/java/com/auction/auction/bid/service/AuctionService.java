package com.auction.auction.bid.service;

import com.auction.auction.bid.exception.AuctionNotFoundException;
import com.auction.auction.bid.kafka.KafkaProducerService;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private BidService bidService;


    public Auction placeBid(String auctionId, Bid bid) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException(auctionId));
        if (bid.getAmount() > auction.getCurrentBid()) {
            bidService.saveBid(bid);
            auction.getBids().add(bid);
            auction.setCurrentBid(bid.getAmount());
            auctionRepository.save(auction);
            kafkaProducerService.sendMessage("auction-notifications",
                    "New bid placed on auction: " + auction.getTitle() + " by " + bid.getBidder());

            return auction;
        } else {
            throw new IllegalArgumentException("The bid amount must be higher than the current bid.");
        }
    }


    public Auction saveOrUpdateAuction(Auction auction) {
        Auction savedAuction = auctionRepository.save(auction);
        kafkaProducerService.sendMessage("auction-notifications", "Auction created or updated: " + savedAuction.getTitle());
        return savedAuction;
    }

    public void deleteAuctionById(String id) {
        if (auctionRepository.existsById(id)) {
            auctionRepository.deleteById(id);
            kafkaProducerService.sendMessage("auction-notifications", "Auction deleted with id: " + id);
        } else {
            throw new AuctionNotFoundException(id);
        }
    }

    public Auction getAuctionById(String id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException(id));
    }

    public Auction getAuctionByDescription(String description) {
        return auctionRepository.findByDescription(description)
                .stream().findFirst()
                .orElseThrow(() -> new AuctionNotFoundException("Auction with description " + description + " not found"));
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
        this.kafkaProducerService.sendMessage("auctions", message);
    }
}
