package com.auction.auction.bid.service;

import com.auction.auction.bid.exception.AuctionNotFoundException;
import com.auction.auction.bid.interfaces.AuctionInterface;
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
public class AuctionService implements AuctionInterface {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private BidService bidService;

    @Override
    public Auction createAuction(Auction auction) {
        Auction savedAuction = auctionRepository.save(auction);
        kafkaProducerService.sendMessage("auction-notifications", "Auction created: " + savedAuction.getTitle());
        return savedAuction;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Auction saveAuction(Auction auction) {
        Auction savedAuction = auctionRepository.save(auction);
        kafkaProducerService.sendMessage("auction-notifications", "Auction saved: " + savedAuction.getTitle());
        return savedAuction;
    }

    @Override
    public void deleteAuctionById(String id) {
        if (auctionRepository.existsById(id)) {
            auctionRepository.deleteById(id);
            kafkaProducerService.sendMessage("auction-notifications", "Auction deleted with id: " + id);
        } else {
            throw new AuctionNotFoundException(id);
        }
    }

    @Override
    public Auction getAuctionById(String id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException(id));
    }

    @Override
    public List<Auction> getAuctionsByOwner(String owner) {
        return auctionRepository.findByOwner(owner);
    }

    @Override
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    // testing kafka producer
//    public void sendMessage(String message) {
//        this.kafkaProducerService.sendMessage("auctions", message);
//    }
}
