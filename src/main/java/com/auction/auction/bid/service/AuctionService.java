package com.auction.auction.bid.service;

import com.auction.auction.bid.exception.AuctionNotFoundException;
import com.auction.auction.bid.exception.InvalidAuctionOperationException;
import com.auction.auction.bid.interfaces.AuctionServiceI;
import com.auction.auction.bid.kafka.KafkaProducerService;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.AuctionStatus;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class AuctionService implements AuctionServiceI {

    private final AuctionRepository auctionRepository;
    private final KafkaProducerService kafkaProducerService;
    private final BidService bidService;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, KafkaProducerService kafkaProducerService, @Lazy BidService bidService) {
        this.auctionRepository = auctionRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.bidService = bidService;
    }

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
        Date startOfHour = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date endOfHour = calendar.getTime();

        return auctionRepository.findAuctionsStartingAt(startOfHour, endOfHour);
    }

    @Override
    public Auction placeBid(String auctionId, Bid bid) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException(auctionId));
        if (bid.getAmount() > auction.getCurrentBid()) {
            bidService.placeBid(auctionId,bid);
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
    public Auction getAuctionById(String id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException(id));
    }

    @Override
    public List<Auction> getAuctionsByOwnerId(String owner) {
        return auctionRepository.findByOwner(owner);
    }

    @Override
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    @Override
    public Auction saveAuction(Auction auction) {
        if (auction.getStatus() == AuctionStatus.IN_PROCESS || auction.getStatus() == AuctionStatus.COMPLETED) {
            throw new InvalidAuctionOperationException("Cannot modify an auction with status: " + auction.getStatus());
        }
        Auction savedAuction = auctionRepository.save(auction);
        kafkaProducerService.sendMessage("auction-notifications", "Auction created or updated: " + savedAuction.getTitle());
        return savedAuction;
    }

    @Override
    public void deleteAuctionById(String id) {
        Auction auction = getAuctionById(id);
        if (auction.getStatus() == AuctionStatus.IN_PROCESS || auction.getStatus() == AuctionStatus.COMPLETED) {
            throw new InvalidAuctionOperationException("Cannot delete an auction with status: " + auction.getStatus());
        }
        auctionRepository.deleteById(id);
        kafkaProducerService.sendMessage("auction-notifications", "Auction deleted with id: " + id);
    }


}
