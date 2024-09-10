package com.auction.auction.bid.repository;

import com.auction.auction.bid.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
