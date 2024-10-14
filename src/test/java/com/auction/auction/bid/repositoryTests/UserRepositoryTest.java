package com.auction.auction.bid.repositoryTests;
import com.auction.auction.bid.model.User;
import com.auction.auction.bid.repository.UserRepository;
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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testFindByUsername() {
        User user1 = new User();
        user1.setUsername("username1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("username2");
        userRepository.save(user2);

        List<User> users = userRepository.findByUsername("username1");
        assertEquals(1, users.size());
        assertEquals("username1", users.get(0).getUsername());
    }

    @Test
    void testFindByEmail() {
        User user1 = new User();
        user1.setEmail("email1@example.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("email2@example.com");
        userRepository.save(user2);

        List<User> users = userRepository.findByEmail("email1@example.com");
        assertEquals(1, users.size());
        assertEquals("email1@example.com", users.get(0).getEmail());
    }

    @Test
    void testExistsByEmail() {
        User user = new User();
        user.setEmail("email@example.com");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("email@example.com");
        assertTrue(exists);
    }
}
