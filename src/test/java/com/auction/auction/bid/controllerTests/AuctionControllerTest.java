package com.auction.auction.bid.controllerTests;

import com.auction.auction.bid.controller.AuctionController;
import com.auction.auction.bid.interfaces.AuctionServiceI;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.model.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuctionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuctionServiceI auctionService;

    @InjectMocks
    private AuctionController auctionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auctionController).build();
    }

    @Test
    void testCreateAuction() throws Exception {
        Auction auction = new Auction();
        auction.setId("1");

        when(auctionService.createAuction(any(Auction.class))).thenReturn(auction);

        mockMvc.perform(post("/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void testCloseAuction() throws Exception {
        mockMvc.perform(post("/auctions/1/close"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auction closed successfully"));
    }

    @Test
    void testGetAuctionsStartingAt() throws Exception {
        when(auctionService.getAuctionsStartingAt(any(Date.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/auctions/startingAt")
                        .param("date", "2023-10-10T10:00:00.000Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testSaveAuction() throws Exception {
        mockMvc.perform(post("/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auction saved successfully"));
    }

    @Test
    void testPlaceBid() throws Exception {
        Auction auction = new Auction();
        auction.setId("1");

        when(auctionService.placeBid(anyString(), any(Bid.class))).thenReturn(auction);

        mockMvc.perform(post("/auctions/1/bid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void testGetAllAuctions() throws Exception {
        when(auctionService.getAllAuctions()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/auctions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAuctionsByOwnerId() throws Exception {
        when(auctionService.getAuctionsByOwnerId(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/auctions/auction/owner1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testDeleteAuctionById() throws Exception {
        mockMvc.perform(delete("/auctions/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auction deleted successfully"));
    }
}