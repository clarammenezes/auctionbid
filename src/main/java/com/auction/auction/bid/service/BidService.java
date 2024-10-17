package com.auction.auction.bid.service;

import com.auction.auction.bid.exception.InvalidBidException;
import com.auction.auction.bid.interfaces.BidServiceI;
import com.auction.auction.bid.kafka.KafkaProducerService;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.AuctionStatus;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BidService implements BidServiceI {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public Bid placeBid(String auctionId, Bid bid) {
        Auction auction = auctionService.getAuctionById(auctionId);

        if (auction.getStatus() != AuctionStatus.IN_PROCESS) {
            throw new InvalidBidException("Cannot place a bid on an auction with status: " + auction.getStatus());
        }

        // fetch all bids for the auction to determine the highest bid
        List<Bid> bids = auction.getBids();
        Bid currentHighestBid = bids.isEmpty() ? null : bids.stream()
                .max(Comparator.comparingDouble(Bid::getAmount))
                .orElse(null);

        if (currentHighestBid != null && bid.getAmount() > currentHighestBid.getAmount()) {
            String outbidMessage = String.format("You have been outbid on auction %s. New highest bid is %.2f.",
                    auction.getTitle(),
                    bid.getAmount());
            kafkaProducerService.sendMessage("user-notifications", outbidMessage);
        }

        // validate the new bid against the current highest bid
        if (currentHighestBid != null && bid.getAmount() <= currentHighestBid.getAmount()) {
            throw new InvalidBidException("Bid amount must be greater than the current highest bid of: " + currentHighestBid.getAmount());
        }

        auction.getBids().add(bid);

        auction.setCurrentBid(bid.getAmount());

        auctionService.saveAuction(auction);

        String newBidMessage = String.format("New bid placed on auction %s by %s: %.2f",
                auction.getTitle(),
                bid.getBidder(),
                bid.getAmount());
        kafkaProducerService.sendMessage("auction-notifications", newBidMessage);

        return bidRepository.save(bid);
    }
}
