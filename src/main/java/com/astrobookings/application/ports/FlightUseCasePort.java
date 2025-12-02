package com.astrobookings.application.ports;

import com.astrobookings.business.dto.CreateFlightDto;
import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.models.FlightStatus;
import java.util.List;

public interface FlightUseCasePort {
    List<Flight> getAllFlights(FlightStatus status);
    Flight createFlight(CreateFlightDto dto);
}