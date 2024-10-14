package com.auction.auction.bid.controllerTests;
import com.auction.auction.bid.controller.BidController;
import com.auction.auction.bid.interfaces.BidIServiceI;
import com.auction.auction.bid.model.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BidController.class)
class BidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidIServiceI bidService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceBid() throws Exception {
        Bid bid = new Bid();
        bid.setAmount(100.0);
        bid.setBidder("bidder1");

        when(bidService.placeBid(anyString(), any(Bid.class))).thenReturn(bid);

        mockMvc.perform(post("/bids/place/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.0, \"bidder\":\"bidder1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.bidder").value("bidder1"));
    }
}
