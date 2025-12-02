package com.astrobookings.presentation;

import com.astrobookings.business.exceptions.BusinessException;
import com.astrobookings.business.exceptions.NotFoundException;
import com.astrobookings.business.exceptions.PaymentException;
import com.astrobookings.business.exceptions.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHandler {
    protected final ObjectMapper objectMapper;

    public BaseHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    protected void sendJsonResponse(HttpExchange exchange, int statusCode, Object response) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(response);
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    protected <T> T parseRequestBody(HttpExchange exchange, Class<T> clazz) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return objectMapper.readValue(is, clazz);
        }
    }

    protected void handleException(HttpExchange exchange, Exception e) throws IOException {
        int statusCode;
        String errorType;

        if (e instanceof ValidationException) {
            statusCode = 400;
            errorType = "ValidationError";
        } else if (e instanceof BusinessException) {
            statusCode = 400;
            errorType = "BusinessRuleViolation";
        } else if (e instanceof NotFoundException) {
            statusCode = 404;
            errorType = "NotFound";
        } else if (e instanceof PaymentException) {
            statusCode = 402;
            errorType = "PaymentRequired";
        } else {
            statusCode = 500;
            errorType = "InternalServerError";
            e.printStackTrace();
        }

        ErrorResponse errorResponse = new ErrorResponse(errorType, e.getMessage());
        sendJsonResponse(exchange, statusCode, errorResponse);
    }

    protected String getQueryParam(HttpExchange exchange, String param) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) return null;

        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(param)) {
                return keyValue[1];
            }
        }
        return null;
    }
}