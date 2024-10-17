package com.auction.auction.bid.serviceTests;

import com.auction.auction.bid.exception.InvalidAuctionOperationException;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.AuctionStatus;
import com.auction.auction.bid.model.Bid;
import com.auction.auction.bid.repository.AuctionRepository;
import com.auction.auction.bid.service.AuctionService;
import com.auction.auction.bid.kafka.KafkaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private AuctionService auctionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAuction() {
        Auction auction = new Auction();
        auction.setTitle("Test Auction");

        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);

        Auction createdAuction = auctionService.createAuction(auction);

        assertNotNull(createdAuction);
        assertEquals("Test Auction", createdAuction.getTitle());
        verify(auctionRepository, times(1)).save(auction);
        verify(kafkaProducerService, times(1)).sendMessage(eq("auction-notifications"), anyString());
    }

    @Test
    void testCloseAuction() {
        String auctionId = "1";
        Auction auction = new Auction();
        auction.setBids(Collections.singletonList(new Bid()));

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        auctionService.closeAuction(auctionId);

        verify(auctionRepository, times(1)).findById(auctionId);
        verify(kafkaProducerService, times(1)).sendMessage(eq("auction-notifications"), anyString());
    }

    @Test
    void testDeleteAuctionById_Success() {
        String auctionId = "1";
        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setStatus(AuctionStatus.CREATED);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        auctionService.deleteAuctionById(auctionId);

        verify(auctionRepository, times(1)).deleteById(auctionId);
        verify(kafkaProducerService, times(1)).sendMessage(eq("auction-notifications"), eq("Auction deleted with id: " + auctionId));
    }

    @Test
    void testDeleteAuctionById_InvalidStatus() {
        String auctionId = "1";
        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setStatus(AuctionStatus.IN_PROCESS);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        assertThrows(InvalidAuctionOperationException.class, () -> auctionService.deleteAuctionById(auctionId));

        verify(auctionRepository, never()).deleteById(auctionId);
        verify(kafkaProducerService, never()).sendMessage(anyString(), anyString());
    }
}