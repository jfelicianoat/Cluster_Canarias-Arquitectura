package com.astrobookings.domain.ports;

import com.astrobookings.domain.models.Booking;
import java.util.List;
import java.util.Optional;

public interface BookingRepositoryPort {
    List<Booking> findAll();
    Optional<Booking> findById(String id);
    List<Booking> findByFlightId(String flightId);
    List<Booking> findByPassengerEmail(String passengerEmail);
    Booking save(Booking booking);
}