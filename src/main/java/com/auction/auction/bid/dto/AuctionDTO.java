package com.auction.auction.bid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;


@Getter
@Setter
public class AuctionDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String seller;

    @Positive
    private double startingBid;

    @Range(min = 0, max = 1000000)
    private double currentBid;
}
