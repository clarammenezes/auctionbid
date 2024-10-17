package com.auction.auction.bid.controllerTests;

import com.auction.auction.bid.controller.UserController;
import com.auction.auction.bid.dto.UserDTO;
import com.auction.auction.bid.interfaces.UserServiceI;
import com.auction.auction.bid.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceI userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User();
        user.setId("1");

        when(userService.createUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\", \"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setId("1");

        when(userService.updateUser(anyString(), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\", \"username\":\"updateduser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setId("1");

        when(userService.getUserById(anyString())).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetUserByUsername() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        when(userService.getUserByUsername(anyString())).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    void testDeleteUserById() throws Exception {
        when(userService.deleteUserById(anyString())).thenReturn("User deleted successfully");

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}