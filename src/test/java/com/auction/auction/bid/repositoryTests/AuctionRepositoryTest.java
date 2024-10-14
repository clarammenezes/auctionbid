package com.auction.auction.bid.repositoryTests;
import com.auction.auction.bid.model.Auction;
import com.auction.auction.bid.repository.AuctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuctionRepositoryTest {

    @Autowired
    private AuctionRepository auctionRepository;

    @BeforeEach
    void setUp() {
        auctionRepository.deleteAll();
    }

    @Test
    void testFindByOwner() {
        Auction auction1 = new Auction();
        auction1.setOwner("owner1");
        auctionRepository.save(auction1);

        Auction auction2 = new Auction();
        auction2.setOwner("owner2");
        auctionRepository.save(auction2);

        List<Auction> owner1Auctions = auctionRepository.findByOwner("owner1");
        assertEquals(1, owner1Auctions.size());
        assertEquals("owner1", owner1Auctions.get(0).getOwner());
    }

    @Test
    void testFindAuctionsStartingAt() {
        Date now = new Date();
        Date later = new Date(now.getTime() + 10000);

        Auction auction1 = new Auction();
        auction1.setStartDate(now);
        auctionRepository.save(auction1);

        Auction auction2 = new Auction();
        auction2.setStartDate(later);
        auctionRepository.save(auction2);

        List<Auction> auctions = auctionRepository.findAuctionsStartingAt(now, new Date(later.getTime() + 10000));
        assertEquals(2, auctions.size());
    }
}