package com.auction.auction.bid.serviceTests;
import com.auction.auction.bid.exception.InvalidBidException;
import com.auction.auction.bid.kafka.KafkaProducerService;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.AuctionStatus;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.BidRepository;
import com.auction.auction.bid.service.AuctionService;
import com.auction.auction.bid.service.BidService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AuctionService auctionService;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private BidService bidService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceBid_Success() {
        String auctionId = "1";
        Bid bid = new Bid();
        bid.setAmount(200.0);
        bid.setBidder("bidder1");

        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setStatus(AuctionStatus.IN_PROCESS);
        auction.setBids(new ArrayList<>());

        when(auctionService.getAuctionById(auctionId)).thenReturn(auction);
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);

        Bid placedBid = bidService.placeBid(auctionId, bid);

        assertNotNull(placedBid);
        assertEquals(200.0, placedBid.getAmount());
        verify(auctionService, times(1)).saveAuction(auction);
        verify(kafkaProducerService, times(1)).sendMessage(eq("auction-notifications"), anyString());
    }

    @Test
    void testPlaceBid_InvalidStatus() {
        String auctionId = "1";
        Bid bid = new Bid();
        bid.setAmount(200.0);

        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setStatus(AuctionStatus.COMPLETED);

        when(auctionService.getAuctionById(auctionId)).thenReturn(auction);

        assertThrows(InvalidBidException.class, () -> bidService.placeBid(auctionId, bid));
        verify(bidRepository, never()).save(any(Bid.class));
        verify(kafkaProducerService, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void testPlaceBid_LowerThanCurrentHighest() {
        String auctionId = "1";
        Bid bid = new Bid();
        bid.setAmount(100.0);

        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setStatus(AuctionStatus.IN_PROCESS);

        Bid highestBid = new Bid();
        highestBid.setAmount(150.0);
        List<Bid> bids = new ArrayList<>();
        bids.add(highestBid);
        auction.setBids(bids);

        when(auctionService.getAuctionById(auctionId)).thenReturn(auction);

        assertThrows(InvalidBidException.class, () -> bidService.placeBid(auctionId, bid));
        verify(bidRepository, never()).save(any(Bid.class));
        verify(kafkaProducerService, never()).sendMessage(anyString(), anyString());
    }
}
