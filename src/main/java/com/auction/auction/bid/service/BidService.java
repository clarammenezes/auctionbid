package com.auction.auction.bid.service;

import com.auction.auction.bid.kafka.KafkaProducerService;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Bid saveBid(Bid bid) {
        Bid savedBid = bidRepository.save(bid);

        String message = String.format("Bid placed: AuctionID=%s, Bidder=%s, Amount=%.2f",
                bid.getAuctionId(),
                savedBid.getBidder(),
                savedBid.getAmount());
        kafkaProducerService.sendMessage("bid-notifications", message);

        return savedBid;
    }

    public Bid placeBid(String auctionId, Bid bid) {
        Auction auction = auctionService.getAuctionById(auctionId);

        auction.getBids().add(bid);
        auctionService.saveOrUpdateAuction(auction);

        return saveBid(bid);
    }

//
//    public Bid saveBid(Bid bid) {
//        return bidRepository.save(bid);
//    }

}
