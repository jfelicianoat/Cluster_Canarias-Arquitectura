package com.astrobookings.infrastructure.presentation.adapters;

import com.astrobookings.application.ports.CancellationUseCasePort;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminHandler extends BaseHandler implements HttpHandler {
    private final CancellationUseCasePort cancellationUseCase;

    public AdminHandler(CancellationUseCasePort cancellationUseCase) {
        this.cancellationUseCase = cancellationUseCase;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        try {
            if ("POST".equals(method) && "/admin/cancel-flights".equals(path)) {
                handleCancelFlights(exchange);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleCancelFlights(HttpExchange exchange) throws IOException {
        int cancelledCount = cancellationUseCase.processCancellations();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cancellation process completed");
        response.put("flightsCancelled", cancelledCount);
        
        sendJsonResponse(exchange, 200, response);
    }
}