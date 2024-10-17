package com.auction.auction.bid.serviceTests;
import com.auction.auction.bid.dto.UserDTO;
import com.auction.auction.bid.exception.EmailAlreadyInUseException;
import com.auction.auction.bid.exception.UserNotFoundException;
import com.auction.auction.bid.model.User;
import com.auction.auction.bid.repository.UserRepository;
import com.auction.auction.bid.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserDTO userDTO = new UserDTO("username", "password", "email@example.com");
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword("encodedPassword");
        user.setEmail(userDTO.getEmail());

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(userDTO.getUsername(), createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailAlreadyInUse() {
        UserDTO userDTO = new UserDTO("username", "password", "email@example.com");

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        String userId = "1";
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
    }

    @Test
    void testGetUserById_NotFound() {
        String userId = "1";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.getAllUsers();

        assertEquals(2, foundUsers.size());
    }

    @Test
    void testUpdateUser_Success() {
        String userId = "1";
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");
        existingUser.setEmail("oldEmail@example.com");

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");
        updatedUser.setPassword("newPassword");
        updatedUser.setEmail("newEmail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        assertEquals("encodedNewPassword", result.getPassword());
        assertEquals("newEmail@example.com", result.getEmail());
    }

    @Test
    void testDeleteUserById_Success() {
        String userId = "1";

        when(userRepository.existsById(userId)).thenReturn(true);

        String result = userService.deleteUserById(userId);

        assertEquals("User deleted successfully.", result);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUserById_NotFound() {
        String userId = "1";

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        verify(userRepository, never()).deleteById(userId);
    }
}