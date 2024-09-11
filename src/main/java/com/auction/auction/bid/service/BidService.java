package com.auction.auction.bid.service;

import com.auction.auction.bid.kafka.KafkaProducerService;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Bid saveBid(String auctionId, Bid bid) {
        Auction auction = auctionService.getAuctionById(auctionId);

        auction.getBids().add(bid);
        auctionService.saveOrUpdateAuction(auction);

        kafkaProducerService.sendMessage("bid-notifications", "New bid placed on auction: " + auction.getTitle());

        return bidRepository.save(bid);
    }

    //place bid on auction
    public Bid placeBid(String auctionId, Bid bid) {
        Auction auction = auctionService.getAuctionById(auctionId);

        // verification
        List<Bid> bids = auction.getBids();
        if (!bids.isEmpty()) {
            Bid currentHighestBid = bids.stream()
                    .max(Comparator.comparingDouble(Bid::getAmount))
                    .orElse(null);
            if (bid.getAmount() > currentHighestBid.getAmount()) {
                // notify user of outbid
                String outbidMessage = String.format("You have been outbid on auction %s. New highest bid is %.2f.",
                        auction.getTitle(),
                        bid.getAmount());
                kafkaProducerService.sendMessage("user-notifications", outbidMessage);
            }
        }

        // new bid to auction
        auction.getBids().add(bid);
        auctionService.saveOrUpdateAuction(auction);

        // new bid placed notification
        String newBidMessage = String.format("New bid placed on auction %s by %s: %.2f",
                auction.getTitle(),
                bid.getBidder(),
                bid.getAmount());
        kafkaProducerService.sendMessage("bid-notifications", newBidMessage);

        return bidRepository.save(bid);
    }
}
