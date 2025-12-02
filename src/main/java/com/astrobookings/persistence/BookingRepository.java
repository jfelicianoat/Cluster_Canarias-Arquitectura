package com.astrobookings.persistence;

import com.astrobookings.persistence.models.Booking;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BookingRepository {
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }

    public List<Booking> findByFlightId(String flightId) {
        return bookings.values().stream()
                .filter(b -> b.getFlightId().equals(flightId))
                .collect(Collectors.toList());
    }

    public List<Booking> findByPassengerEmail(String passengerEmail) {
        return bookings.values().stream()
                .filter(b -> b.getPassengerEmail().equalsIgnoreCase(passengerEmail))
                .collect(Collectors.toList());
    }

    public Booking save(Booking booking) {
        String id = booking.getId() != null ? booking.getId() : "b" + idCounter.getAndIncrement();
        Booking bookingToSave = new Booking(id, booking.getFlightId(), booking.getPassengerEmail(),
                                           booking.getFinalPrice(), booking.getPaymentTransactionId(),
                                           booking.getCreatedAt());
        bookings.put(id, bookingToSave);
        return bookingToSave;
    }
}