package com.auction.auction.bid.controllerTests;

import com.auction.auction.bid.controller.BidController;
import com.auction.auction.bid.interfaces.BidServiceI;
import com.auction.auction.bid.model.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BidControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BidServiceI bidService;

    @InjectMocks
    private BidController bidController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
    }

    @Test
    void testPlaceBid() throws Exception {
        Bid bid = new Bid();
        bid.setId("1");

        when(bidService.placeBid(anyString(), any(Bid.class))).thenReturn(bid);

        mockMvc.perform(post("/bids/place/auction123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));
    }
}