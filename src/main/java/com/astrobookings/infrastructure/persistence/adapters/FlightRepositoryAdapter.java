package com.astrobookings.infrastructure.persistence.adapters;

import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.models.FlightStatus;
import com.astrobookings.domain.ports.FlightRepositoryPort;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FlightRepositoryAdapter implements FlightRepositoryPort {
    private final Map<String, Flight> flights = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public List<Flight> findAll() {
        return new ArrayList<>(flights.values());
    }

    @Override
    public List<Flight> findAllFuture() {
        LocalDateTime now = LocalDateTime.now();
        return flights.values().stream()
                .filter(f -> f.getDepartureDate().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> findByStatus(FlightStatus status) {
        return flights.values().stream()
                .filter(f -> f.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Flight> findById(String id) {
        return Optional.ofNullable(flights.get(id));
    }

    @Override
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

    @Override
    public void update(Flight flight) {
        flights.put(flight.getId(), flight);
    }

    @Override
    public List<Flight> findScheduledFlightsNeedingCancellation() {
        LocalDateTime oneWeekFromNow = LocalDateTime.now().plusWeeks(1);
        return flights.values().stream()
                .filter(f -> f.getStatus() == FlightStatus.SCHEDULED)
                .filter(f -> f.getDepartureDate().isBefore(oneWeekFromNow))
                .filter(f -> f.getBookedSeats() < f.getMinPassengers())
                .collect(Collectors.toList());
    }
}