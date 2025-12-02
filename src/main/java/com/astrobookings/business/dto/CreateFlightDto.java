package com.astrobookings.business.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class CreateFlightDto {
    private final String rocketId;
    private final LocalDateTime departureDate;
    private final Double basePrice;
    private final Integer minPassengers;

    @JsonCreator
    public CreateFlightDto(
            @JsonProperty("rocketId") String rocketId,
            @JsonProperty("departureDate") LocalDateTime departureDate,
            @JsonProperty("basePrice") Double basePrice,
            @JsonProperty("minPassengers") Integer minPassengers) {
        this.rocketId = rocketId;
        this.departureDate = departureDate;
        this.basePrice = basePrice;
        this.minPassengers = minPassengers;
    }

    public String getRocketId() {
        return rocketId;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public Integer getMinPassengers() {
        return minPassengers;
    }
}