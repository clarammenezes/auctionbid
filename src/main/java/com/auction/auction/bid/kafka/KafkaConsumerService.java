package com.auction.auction.bid.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "auction-created", groupId = "auction-group")
    public void listenAuctionCreated(String message) {
        System.out.println("Message received (auction created): " + message);
    }

    @KafkaListener(topics = "bid-placed", groupId = "auction-group")
    public void listenBidPlaced(String message) {
        System.out.println("Message received (bid placed): " + message);
    }
}
