package com.astrobookings.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    private final String error;
    private final String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    @JsonProperty("error")
    public String getError() {
        return error;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}