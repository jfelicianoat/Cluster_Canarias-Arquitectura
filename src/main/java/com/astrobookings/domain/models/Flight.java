package com.astrobookings.domain.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Flight {
    private final String id;
    private final String rocketId;
    private final LocalDateTime departureDate;
    private final double basePrice;
    private final int minPassengers;
    private FlightStatus status;
    private int bookedSeats;

    @JsonCreator
    public Flight(
            @JsonProperty("id") String id,
            @JsonProperty("rocketId") String rocketId,
            @JsonProperty("departureDate") LocalDateTime departureDate,
            @JsonProperty("basePrice") double basePrice,
            @JsonProperty("minPassengers") int minPassengers) {
        this.id = id;
        this.rocketId = rocketId;
        this.departureDate = departureDate;
        this.basePrice = basePrice;
        this.minPassengers = minPassengers > 0 ? minPassengers : 5; // Default to 5
        this.status = FlightStatus.SCHEDULED;
        this.bookedSeats = 0;
    }

    public String getId() {
        return id;
    }

    public String getRocketId() {
        return rocketId;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getMinPassengers() {
        return minPassengers;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void incrementBookedSeats() {
        this.bookedSeats++;
    }
}