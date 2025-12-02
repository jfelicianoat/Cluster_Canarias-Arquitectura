package com.astrobookings.domain.ports;

public interface PaymentGatewayPort {
    String processPayment(String passengerEmail, double amount);
    void processRefund(String transactionId, double amount);
}