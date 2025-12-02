package com.astrobookings.application.usecases;

import com.astrobookings.application.ports.FlightUseCasePort;
import com.astrobookings.business.dto.CreateFlightDto;
import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.models.FlightStatus;
import com.astrobookings.domain.services.FlightDomainService;
import java.util.List;

public class FlightUseCase implements FlightUseCasePort {
    private final FlightDomainService flightDomainService;

    public FlightUseCase(FlightDomainService flightDomainService) {
        this.flightDomainService = flightDomainService;
    }

    @Override
    public List<Flight> getAllFlights(FlightStatus status) {
        return flightDomainService.getAllFlights(status);
    }

    @Override
    public Flight createFlight(CreateFlightDto dto) {
        return flightDomainService.createFlight(dto);
    }
}