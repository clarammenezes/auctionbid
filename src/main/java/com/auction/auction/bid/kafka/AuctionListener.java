package com.auction.auction.bid.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuctionListener {

    @KafkaListener(topics = "auctions")
    public void listen(String message) {
        System.out.println("Received Messasge in group foo: " + message);
    }

}
