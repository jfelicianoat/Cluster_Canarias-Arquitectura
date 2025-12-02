package com.astrobookings.business;

import com.astrobookings.persistence.BookingRepository;
import com.astrobookings.persistence.FlightRepository;
import com.astrobookings.persistence.models.Booking;
import com.astrobookings.persistence.models.Flight;
import com.astrobookings.persistence.models.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CancellationService {
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final PaymentGateway paymentGateway;
    private final NotificationService notificationService;

    public CancellationService(FlightRepository flightRepository,
                              BookingRepository bookingRepository,
                              PaymentGateway paymentGateway,
                              NotificationService notificationService) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
    }

    public int processCancellations() {
        System.out.println("\n===== PROCESSING FLIGHT CANCELLATIONS =====");
        System.out.println("Checking for flights that need cancellation...");
        System.out.println("Current time: " + LocalDateTime.now());

        // Find flights that need cancellation
        List<Flight> flightsToCancel = flightRepository.findScheduledFlightsNeedingCancellation();

        if (flightsToCancel.isEmpty()) {
            System.out.println("No flights need cancellation at this time.");
            System.out.println("===========================================\n");
            return 0;
        }

        System.out.println("Found " + flightsToCancel.size() + " flight(s) to cancel:");

        for (Flight flight : flightsToCancel) {
            System.out.printf("- Flight %s (departure: %s, booked: %d/%d)%n", 
                            flight.getId(), 
                            flight.getDepartureDate(),
                            flight.getBookedSeats(),
                            flight.getMinPassengers());
            
            // Get all bookings for this flight
            List<Booking> bookings = bookingRepository.findByFlightId(flight.getId());
            
            if (!bookings.isEmpty()) {
                // Process refunds
                System.out.println("  Processing refunds for " + bookings.size() + " passengers:");
                for (Booking booking : bookings) {
                    paymentGateway.processRefund(booking.getPaymentTransactionId(), booking.getFinalPrice());
                    System.out.printf("    - Refunded %.2f to %s%n", 
                                    booking.getFinalPrice(), 
                                    booking.getPassengerEmail());
                }

                // Send cancellation notifications
                List<String> passengerEmails = bookings.stream()
                        .map(Booking::getPassengerEmail)
                        .collect(Collectors.toList());
                notificationService.sendFlightCancelledNotification(flight, passengerEmails);
            }

            // Update flight status
            flight.setStatus(FlightStatus.CANCELLED);
            flightRepository.update(flight);
            System.out.println("  Flight " + flight.getId() + " has been cancelled.");
        }

        System.out.println("\nCancellation process completed. " + flightsToCancel.size() + " flight(s) cancelled.");
        System.out.println("===========================================\n");

        return flightsToCancel.size();
    }
}