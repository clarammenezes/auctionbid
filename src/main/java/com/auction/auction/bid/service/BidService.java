package com.auction.auction.bid.service;

import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    public Bid saveBid(Bid bid) {
        return bidRepository.save(bid);
    }

}
