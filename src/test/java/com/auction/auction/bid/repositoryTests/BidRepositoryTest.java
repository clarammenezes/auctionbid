package com.auction.auction.bid.repositoryTests;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.BidRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;

    @BeforeEach
    void setUp() {
        bidRepository.deleteAll();
    }

    @Test
    void testSaveAndFindBid() {
        Bid bid = new Bid();
        bid.setAmount(100.0);
        bid.setBidder("bidder1");

        bidRepository.save(bid);

        List<Bid> bids = bidRepository.findAll();
        assertEquals(1, bids.size());
        assertEquals(100.0, bids.get(0).getAmount());
        assertEquals("bidder1", bids.get(0).getBidder());
    }
}
