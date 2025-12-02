package com.astrobookings.infrastructure.presentation.adapters;

import com.astrobookings.application.ports.BookingUseCasePort;
import com.astrobookings.business.dto.CreateBookingDto;
import com.astrobookings.domain.models.Booking;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class BookingHandler extends BaseHandler implements HttpHandler {
    private final BookingUseCasePort bookingUseCase;

    public BookingHandler(BookingUseCasePort bookingUseCase) {
        this.bookingUseCase = bookingUseCase;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        
        try {
            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String flightId = getQueryParam(exchange, "flightId");
        String passengerEmail = getQueryParam(exchange, "passengerEmail");
        
        List<Booking> bookings = bookingUseCase.getAllBookings(flightId, passengerEmail);
        sendJsonResponse(exchange, 200, bookings);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        CreateBookingDto dto = parseRequestBody(exchange, CreateBookingDto.class);
        Booking booking = bookingUseCase.createBooking(dto);
        sendJsonResponse(exchange, 201, booking);
    }
}