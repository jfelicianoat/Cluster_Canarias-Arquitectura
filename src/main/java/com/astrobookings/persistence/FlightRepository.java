package com.astrobookings.persistence;

import com.astrobookings.persistence.models.Flight;
import com.astrobookings.persistence.models.FlightStatus;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FlightRepository {
    private final Map<String, Flight> flights = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public List<Flight> findAll() {
        return new ArrayList<>(flights.values());
    }

    public List<Flight> findAllFuture() {
        LocalDateTime now = LocalDateTime.now();
        return flights.values().stream()
                .filter(f -> f.getDepartureDate().isAfter(now))
                .collect(Collectors.toList());
    }

    public List<Flight> findByStatus(FlightStatus status) {
        return flights.values().stream()
                .filter(f -> f.getStatus() == status)
                .collect(Collectors.toList());
    }

    public Optional<Flight> findById(String id) {
        return Optional.ofNullable(flights.get(id));
    }

    public Flight save(Flight flight) {
        String id = flight.getId() != null ? flight.getId() : "f" + idCounter.getAndIncrement();
        Flight flightToSave = new Flight(id, flight.getRocketId(), flight.getDepartureDate(), 
                                        flight.getBasePrice(), flight.getMinPassengers());
        if (flight.getStatus() != null) {
            flightToSave.setStatus(flight.getStatus());
        }
        for (int i = 0; i < flight.getBookedSeats(); i++) {
            flightToSave.incrementBookedSeats();
        }
        flights.put(id, flightToSave);
        return flightToSave;
    }

    public void update(Flight flight) {
        flights.put(flight.getId(), flight);
    }

    public List<Flight> findScheduledFlightsNeedingCancellation() {
        LocalDateTime oneWeekFromNow = LocalDateTime.now().plusWeeks(1);
        return flights.values().stream()
                .filter(f -> f.getStatus() == FlightStatus.SCHEDULED)
                .filter(f -> f.getDepartureDate().isBefore(oneWeekFromNow))
                .filter(f -> f.getBookedSeats() < f.getMinPassengers())
                .collect(Collectors.toList());
    }
}