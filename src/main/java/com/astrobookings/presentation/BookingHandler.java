package com.astrobookings.presentation;

import com.astrobookings.business.BookingService;
import com.astrobookings.business.dto.CreateBookingDto;
import com.astrobookings.persistence.models.Booking;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class BookingHandler extends BaseHandler implements HttpHandler {
    private final BookingService bookingService;

    public BookingHandler(BookingService bookingService) {
        this.bookingService = bookingService;
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
        
        List<Booking> bookings = bookingService.getAllBookings(flightId, passengerEmail);
        sendJsonResponse(exchange, 200, bookings);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        CreateBookingDto dto = parseRequestBody(exchange, CreateBookingDto.class);
        Booking booking = bookingService.createBooking(dto);
        sendJsonResponse(exchange, 201, booking);
    }
}