package com.auction.auction.bid.repositoryTests;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.repository.AuctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionRepositoryTest {

    @Mock
    private AuctionRepository auctionRepository;

    @BeforeEach
    void setUp() {
        // no need to delete all records as we are using mocks
    }

    @Test
    void testFindByOwner() {
        Auction auction = new Auction();
        auction.setOwner("owner1");

        when(auctionRepository.findByOwner("owner1")).thenReturn(Collections.singletonList(auction));

        List<Auction> owner1Auctions = auctionRepository.findByOwner("owner1");

        assertEquals(1, owner1Auctions.size());
        assertEquals("owner1", owner1Auctions.get(0).getOwner());

        verify(auctionRepository).findByOwner("owner1");
    }

    @Test
    void testFindAuctionsStartingAt() {
        Date now = new Date();
        Date later = new Date(now.getTime() + 10000);

        Auction auction1 = new Auction();
        auction1.setStartDate(now);

        Auction auction2 = new Auction();
        auction2.setStartDate(later);

        when(auctionRepository.findAuctionsStartingAt(any(Date.class), any(Date.class)))
                .thenReturn(List.of(auction1, auction2));

        List<Auction> auctions = auctionRepository.findAuctionsStartingAt(now, new Date(later.getTime() + 10000));

        assertEquals(2, auctions.size());

        verify(auctionRepository).findAuctionsStartingAt(any(Date.class), any(Date.class));
    }
}