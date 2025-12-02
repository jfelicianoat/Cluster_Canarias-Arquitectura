package com.astrobookings.domain.ports;

import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.models.FlightStatus;
import java.util.List;
import java.util.Optional;

public interface FlightRepositoryPort {
    List<Flight> findAll();
    List<Flight> findAllFuture();
    List<Flight> findByStatus(FlightStatus status);
    Optional<Flight> findById(String id);
    Flight save(Flight flight);
    void update(Flight flight);
    List<Flight> findScheduledFlightsNeedingCancellation();
}