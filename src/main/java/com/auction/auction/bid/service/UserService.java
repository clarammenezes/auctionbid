package com.auction.auction.bid.service;

import com.auction.auction.bid.exception.UserNotFoundException;
import com.auction.auction.bid.model.User;
import com.auction.auction.bid.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveOrUpdateUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public List<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUserById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
