package com.auction.auction.bid.service;

import com.auction.auction.bid.exception.AuctionNotFoundException;
import com.auction.auction.bid.kafka.KafkaProducerService;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private BidService bidService;

    // create auction
    public Auction createAuction(Auction auction) {
        Auction savedAuction = auctionRepository.save(auction);
        kafkaProducerService.sendMessage("auction-notifications", "Auction created: " + savedAuction.getTitle());
        return savedAuction;
    }

    public List<Auction> getAuctionsStartingAt(Date specificDate) {
        // create a date range for the specific date and hour
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(specificDate);

        // set the start of the hour (e.g., 2023-10-15 10:00:00)
        Date startOfHour = calendar.getTime();

        // set the end of the hour (e.g., 2023-10-15 10:59:59)
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date endOfHour = calendar.getTime();

        // call the repository method to find auctions
        return auctionRepository.findAuctionsStartingAt(startOfHour, endOfHour);
    }

    // place bid on auction
    public Auction placeBid(String auctionId, Bid bid) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException(auctionId));
        if (bid.getAmount() > auction.getCurrentBid()) {
            bidService.saveBid(auctionId,bid);
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

    // close auction
    public void closeAuction(String auctionId) {
        Auction auction = getAuctionById(auctionId);

        // verification
        List<Bid> bids = auction.getBids();
        if (bids.isEmpty()) {
            throw new AuctionNotFoundException("No bids found for this auction.");
        }

        Bid winningBid = bids.stream()
                .max(Comparator.comparingDouble(Bid::getAmount))
                .orElseThrow(() -> new AuctionNotFoundException("No valid bids found."));

        // notify winner
        String message = String.format("Congratulations %s, you won the auction %s with a bid of %.2f",
                winningBid.getBidder(),
                auction.getTitle(),
                winningBid.getAmount());
        kafkaProducerService.sendMessage("auction-notifications", message);

        auction.setClosed(true);
        auctionRepository.save(auction);
    }


    public Auction saveOrUpdateAuction(Auction auction) {
        Auction savedAuction = auctionRepository.save(auction);
        kafkaProducerService.sendMessage("auction-notifications", "Auction updated: " + savedAuction.getTitle());
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

    public Auction getAuctionByCurrentBid(double currentBid) {
        return auctionRepository.findByCurrentBid(currentBid)
                .stream()
                .findFirst()
                .orElseThrow(() -> new AuctionNotFoundException("Auction with current bid " + currentBid + " not found"));
    }

    public Auction getAuctionById(String id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException(id));
    }

    public void deleteAllAuctions() {
        auctionRepository.deleteAll();
        kafkaProducerService.sendMessage("auction-notifications", "All auctions have been deleted.");
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

    // testing kafka producer
    public void sendMessage(String message) {
        this.kafkaProducerService.sendMessage("auctions", message);
    }
}
