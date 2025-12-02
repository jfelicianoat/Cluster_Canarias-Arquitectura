package com.astrobookings.application.usecases;

import com.astrobookings.application.ports.BookingUseCasePort;
import com.astrobookings.business.dto.CreateBookingDto;
import com.astrobookings.domain.models.Booking;
import com.astrobookings.domain.services.BookingDomainService;
import java.util.List;

public class BookingUseCase implements BookingUseCasePort {
    private final BookingDomainService bookingDomainService;

    public BookingUseCase(BookingDomainService bookingDomainService) {
        this.bookingDomainService = bookingDomainService;
    }

    @Override
    public List<Booking> getAllBookings(String flightId, String passengerEmail) {
        return bookingDomainService.getAllBookings(flightId, passengerEmail);
    }

    @Override
    public Booking createBooking(CreateBookingDto dto) {
        return bookingDomainService.createBooking(dto);
    }
}