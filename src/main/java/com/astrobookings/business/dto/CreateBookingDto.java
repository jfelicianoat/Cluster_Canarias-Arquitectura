package com.astrobookings.business.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateBookingDto {
    private final String flightId;
    private final String passengerEmail;

    @JsonCreator
    public CreateBookingDto(
            @JsonProperty("flightId") String flightId,
            @JsonProperty("passengerEmail") String passengerEmail) {
        this.flightId = flightId;
        this.passengerEmail = passengerEmail;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }
}