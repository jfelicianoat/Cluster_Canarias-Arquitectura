package com.astrobookings.infrastructure.presentation.adapters;

import com.astrobookings.application.ports.RocketUseCasePort;
import com.astrobookings.business.dto.CreateRocketDto;
import com.astrobookings.domain.models.Rocket;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class RocketHandler extends BaseHandler implements HttpHandler {
    private final RocketUseCasePort rocketUseCase;

    public RocketHandler(RocketUseCasePort rocketUseCase) {
        this.rocketUseCase = rocketUseCase;
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
        List<Rocket> rockets = rocketUseCase.getAllRockets();
        sendJsonResponse(exchange, 200, rockets);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        CreateRocketDto dto = parseRequestBody(exchange, CreateRocketDto.class);
        Rocket rocket = rocketUseCase.createRocket(dto);
        sendJsonResponse(exchange, 201, rocket);
    }
}