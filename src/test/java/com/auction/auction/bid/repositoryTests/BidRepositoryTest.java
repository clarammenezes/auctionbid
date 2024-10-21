package com.auction.auction.bid.repositoryTests;

import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.BidRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidRepositoryTest {

    @Mock
    private BidRepository bidRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testSaveAndFindBid() {
        Bid bid = new Bid();
        bid.setAmount(100.0);
        bid.setBidder("bidder1");

        when(bidRepository.save(any(Bid.class))).thenReturn(bid);
        when(bidRepository.findAll()).thenReturn(Collections.singletonList(bid));

        bidRepository.save(bid);

        List<Bid> bids = bidRepository.findAll();
        assertEquals(1, bids.size());
        assertEquals(100.0, bids.get(0).getAmount());
        assertEquals("bidder1", bids.get(0).getBidder());

        verify(bidRepository).save(any(Bid.class));
        verify(bidRepository).findAll();
    }
}