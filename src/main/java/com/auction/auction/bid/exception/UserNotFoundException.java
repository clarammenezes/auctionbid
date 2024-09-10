package com.auction.auction.bid.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id) {
        super("User not found with id: " + id);
    }
}