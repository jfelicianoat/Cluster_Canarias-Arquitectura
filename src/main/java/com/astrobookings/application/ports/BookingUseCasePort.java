package com.astrobookings.application.ports;

import com.astrobookings.business.dto.CreateBookingDto;
import com.astrobookings.domain.models.Booking;
import java.util.List;

public interface BookingUseCasePort {
    List<Booking> getAllBookings(String flightId, String passengerEmail);
    Booking createBooking(CreateBookingDto dto);
}