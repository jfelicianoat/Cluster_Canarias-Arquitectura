package com.astrobookings.presentation;

import com.astrobookings.business.FlightService;
import com.astrobookings.business.dto.CreateFlightDto;
import com.astrobookings.persistence.models.Flight;
import com.astrobookings.persistence.models.FlightStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class FlightHandler extends BaseHandler implements HttpHandler {
    private final FlightService flightService;

    public FlightHandler(FlightService flightService) {
        this.flightService = flightService;
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
        String statusParam = getQueryParam(exchange, "status");
        FlightStatus status = null;
        
        if (statusParam != null && !statusParam.isEmpty()) {
            try {
                status = FlightStatus.valueOf(statusParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid status, will return all future flights
            }
        }
        
        List<Flight> flights = flightService.getAllFlights(status);
        sendJsonResponse(exchange, 200, flights);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        CreateFlightDto dto = parseRequestBody(exchange, CreateFlightDto.class);
        Flight flight = flightService.createFlight(dto);
        sendJsonResponse(exchange, 201, flight);
    }
}