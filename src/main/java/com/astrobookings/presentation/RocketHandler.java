package com.astrobookings.presentation;

import com.astrobookings.business.RocketService;
import com.astrobookings.business.dto.CreateRocketDto;
import com.astrobookings.persistence.models.Rocket;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class RocketHandler extends BaseHandler implements HttpHandler {
    private final RocketService rocketService;

    public RocketHandler(RocketService rocketService) {
        this.rocketService = rocketService;
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
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<Rocket> rockets = rocketService.getAllRockets();
        sendJsonResponse(exchange, 200, rockets);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        CreateRocketDto dto = parseRequestBody(exchange, CreateRocketDto.class);
        Rocket rocket = rocketService.createRocket(dto);
        sendJsonResponse(exchange, 201, rocket);
    }
}