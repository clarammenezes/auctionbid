package com.auction.auction.bid.service;

import com.auction.auction.bid.exception.EmailAlreadyInUseException;
import com.auction.auction.bid.exception.UserNotFoundException;
import com.auction.auction.bid.model.User;
import com.auction.auction.bid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyInUseException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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
