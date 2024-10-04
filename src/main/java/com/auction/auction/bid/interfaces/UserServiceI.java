package com.auction.auction.bid.interfaces;


import com.auction.auction.bid.dto.UserDTO;
import com.auction.auction.bid.model.User;

import java.util.List;

public interface UserServiceI {
    User saveUser(User user);
    User createUser(UserDTO userDTO);
    User getUserById(String id);
    User updateUser(String id, User user);
    String deleteUserById(String userId);
    List<User> getAllUsers();
    List<User> getUserByUsername(String username);

}
