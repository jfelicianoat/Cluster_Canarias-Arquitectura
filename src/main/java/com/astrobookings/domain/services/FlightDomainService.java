package com.astrobookings.domain.services;

import com.astrobookings.business.dto.CreateFlightDto;
import com.astrobookings.business.exceptions.BusinessException;
import com.astrobookings.business.exceptions.NotFoundException;
import com.astrobookings.business.exceptions.ValidationException;
import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.models.FlightStatus;
import com.astrobookings.domain.ports.FlightRepositoryPort;
import com.astrobookings.domain.ports.RocketRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

public class FlightDomainService {
    private final FlightRepositoryPort flightRepository;
    private final RocketRepositoryPort rocketRepository;

    public FlightDomainService(FlightRepositoryPort flightRepository, RocketRepositoryPort rocketRepository) {
        this.flightRepository = flightRepository;
        this.rocketRepository = rocketRepository;
    }

    public List<Flight> getAllFlights(FlightStatus status) {
        if (status != null) {
            return flightRepository.findByStatus(status);
        }
        return flightRepository.findAllFuture();
    }

    public Flight createFlight(CreateFlightDto dto) {
        // Validate structure (DTO validation)
        if (dto.getRocketId() == null || dto.getRocketId().trim().isEmpty()) {
            throw new ValidationException("Rocket ID must be provided");
        }
        if (dto.getDepartureDate() == null) {
            throw new ValidationException("Departure date must be provided");
        }
        if (dto.getBasePrice() == null) {
            throw new ValidationException("Base price must be provided");
        }

        // Business validation
        validateFlightBusinessRules(dto);

        int minPassengers = dto.getMinPassengers() != null ? dto.getMinPassengers() : 5;
        Flight flight = new Flight(null, dto.getRocketId(), dto.getDepartureDate(), 
                                  dto.getBasePrice(), minPassengers);
        return flightRepository.save(flight);
    }

    private void validateFlightBusinessRules(CreateFlightDto dto) {
        // Check if rocket exists
        rocketRepository.findById(dto.getRocketId())
                .orElseThrow(() -> new NotFoundException("Rocket not found: " + dto.getRocketId()));

        // Departure date must be in the future
        if (!dto.getDepartureDate().isAfter(LocalDateTime.now())) {
            throw new BusinessException("Departure date must be in the future");
        }

        // Base price must be positive
        if (dto.getBasePrice() <= 0) {
            throw new BusinessException("Base price must be greater than 0");
        }

        // Min passengers validation
        if (dto.getMinPassengers() != null && dto.getMinPassengers() < 1) {
            throw new BusinessException("Minimum passengers must be at least 1");
        }
    }

    public Flight getFlightById(String id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight not found: " + id));
    }
}