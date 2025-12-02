package com.astrobookings.infrastructure.persistence.adapters;

import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.ports.NotificationServicePort;
import java.util.List;

public class NotificationServiceAdapter implements NotificationServicePort {

    @Override
    public void sendFlightConfirmedNotification(Flight flight, List<String> passengerEmails) {
        System.out.println("\n===== FLIGHT CONFIRMED NOTIFICATION =====");
        System.out.printf("Flight %s has been CONFIRMED!%n", flight.getId());
        System.out.printf("Departure: %s%n", flight.getDepartureDate());
        System.out.println("Sending confirmation emails to " + passengerEmails.size() + " passengers:");
        passengerEmails.forEach(email -> System.out.println("  - Email sent to: " + email));
        System.out.println("==========================================\n");
    }

    @Override
    public void sendFlightCancelledNotification(Flight flight, List<String> passengerEmails) {
        System.out.println("\n===== FLIGHT CANCELLED NOTIFICATION =====");
        System.out.printf("Flight %s has been CANCELLED%n", flight.getId());
        System.out.printf("Departure was scheduled for: %s%n", flight.getDepartureDate());
        System.out.println("Sending cancellation emails to " + passengerEmails.size() + " passengers:");
        passengerEmails.forEach(email -> System.out.println("  - Cancellation email sent to: " + email));
        System.out.println("All payments will be refunded.");
        System.out.println("==========================================\n");
    }

    @Override
    public void sendBookingConfirmation(String passengerEmail, String flightId, double finalPrice) {
        System.out.println("\n===== BOOKING CONFIRMATION =====");
        System.out.printf("Booking confirmed for %s%n", passengerEmail);
        System.out.printf("Flight: %s%n", flightId);
        System.out.printf("Final price: %.2f%n", finalPrice);
        System.out.println("=================================\n");
    }
}