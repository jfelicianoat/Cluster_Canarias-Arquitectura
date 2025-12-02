package com.astrobookings.persistence.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Booking {
    private final String id;
    private final String flightId;
    private final String passengerEmail;
    private final double finalPrice;
    private final String paymentTransactionId;
    private final LocalDateTime createdAt;

    @JsonCreator
    public Booking(
            @JsonProperty("id") String id,
            @JsonProperty("flightId") String flightId,
            @JsonProperty("passengerEmail") String passengerEmail,
            @JsonProperty("finalPrice") double finalPrice,
            @JsonProperty("paymentTransactionId") String paymentTransactionId,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.id = id;
        this.flightId = flightId;
        this.passengerEmail = passengerEmail;
        this.finalPrice = finalPrice;
        this.paymentTransactionId = paymentTransactionId;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}