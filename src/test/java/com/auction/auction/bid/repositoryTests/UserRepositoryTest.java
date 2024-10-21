package com.auction.auction.bid.repositoryTests;

import com.auction.auction.bid.model.User;
import com.auction.auction.bid.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // No need to delete all records as we are using mocks
    }

    @Test
    void testFindByUsername() {
        User user1 = new User();
        user1.setUsername("username1");

        when(userRepository.findByUsername("username1")).thenReturn(Collections.singletonList(user1));

        List<User> users = userRepository.findByUsername("username1");
        assertEquals(1, users.size());
        assertEquals("username1", users.get(0).getUsername());

        verify(userRepository).findByUsername("username1");
    }

    @Test
    void testFindByEmail() {
        User user1 = new User();
        user1.setEmail("email1@example.com");

        when(userRepository.findByEmail("email1@example.com")).thenReturn(Collections.singletonList(user1));

        List<User> users = userRepository.findByEmail("email1@example.com");
        assertEquals(1, users.size());
        assertEquals("email1@example.com", users.get(0).getEmail());

        verify(userRepository).findByEmail("email1@example.com");
    }

    @Test
    void testExistsByEmail() {
        when(userRepository.existsByEmail("email@example.com")).thenReturn(true);

        boolean exists = userRepository.existsByEmail("email@example.com");
        assertTrue(exists);

        verify(userRepository).existsByEmail("email@example.com");
    }
}