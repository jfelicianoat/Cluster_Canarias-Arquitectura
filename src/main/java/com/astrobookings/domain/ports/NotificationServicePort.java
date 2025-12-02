package com.astrobookings.domain.ports;

import com.astrobookings.domain.models.Flight;
import java.util.List;

public interface NotificationServicePort {
    void sendFlightConfirmedNotification(Flight flight, List<String> passengerEmails);
    void sendFlightCancelledNotification(Flight flight, List<String> passengerEmails);
    void sendBookingConfirmation(String passengerEmail, String flightId, double finalPrice);
}