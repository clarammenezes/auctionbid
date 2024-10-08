package com.auction.auction.bid.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document (collection = "user")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String role = "USER";

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
