package com.astrobookings.business;

import com.astrobookings.business.exceptions.PaymentException;
import java.util.UUID;
import java.util.Random;

public class PaymentGateway {
    private final Random random = new Random();

    public String processPayment(String passengerEmail, double amount) {
        System.out.printf("Processing payment of %.2f for %s%n", amount, passengerEmail);
        
        // Simulate payment processing with 95% success rate
        if (random.nextDouble() < 0.05) {
            throw new PaymentException("Payment failed for " + passengerEmail);
        }
        
        String transactionId = "PAY-" + UUID.randomUUID().toString();
        System.out.println("Payment successful. Transaction ID: " + transactionId);
        return transactionId;
    }

    public void processRefund(String transactionId, double amount) {
        System.out.printf("Processing refund of %.2f for transaction %s%n", amount, transactionId);
        // Simulate refund processing
        System.out.println("Refund processed successfully for transaction: " + transactionId);
    }
}