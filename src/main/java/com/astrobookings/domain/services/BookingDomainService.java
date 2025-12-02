package com.astrobookings.domain.services;

import com.astrobookings.business.dto.CreateBookingDto;
import com.astrobookings.business.exceptions.BusinessException;
import com.astrobookings.business.exceptions.NotFoundException;
import com.astrobookings.business.exceptions.ValidationException;
import com.astrobookings.domain.models.Booking;
import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.models.FlightStatus;
import com.astrobookings.domain.models.Rocket;
import com.astrobookings.domain.ports.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class BookingDomainService {
    private final BookingRepositoryPort bookingRepository;
    private final FlightRepositoryPort flightRepository;
    private final RocketRepositoryPort rocketRepository;
    private final PaymentGatewayPort paymentGateway;
    private final NotificationServicePort notificationService;

    public BookingDomainService(BookingRepositoryPort bookingRepository, 
                               FlightRepositoryPort flightRepository,
                               RocketRepositoryPort rocketRepository,
                               PaymentGatewayPort paymentGateway, 
                               NotificationServicePort notificationService) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.rocketRepository = rocketRepository;
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
    }

    public List<Booking> getAllBookings(String flightId, String passengerEmail) {
        if (flightId != null && !flightId.trim().isEmpty()) {
            return bookingRepository.findByFlightId(flightId);
        }
        if (passengerEmail != null && !passengerEmail.trim().isEmpty()) {
            return bookingRepository.findByPassengerEmail(passengerEmail);
        }
        return bookingRepository.findAll();
    }

    public Booking createBooking(CreateBookingDto dto) {
        // Validate structure (DTO validation)
        if (dto.getFlightId() == null || dto.getFlightId().trim().isEmpty()) {
            throw new ValidationException("Flight ID must be provided");
        }
        if (dto.getPassengerEmail() == null || dto.getPassengerEmail().trim().isEmpty()) {
            throw new ValidationException("Passenger email must be provided");
        }
        if (!dto.getPassengerEmail().contains("@")) {
            throw new ValidationException("Invalid email format");
        }

        // Get flight and validate
        Flight flight = flightRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new NotFoundException("Flight not found: " + dto.getFlightId()));

        // Business validation
        validateBookingBusinessRules(flight);

        // Get rocket to check capacity
        Rocket rocket = rocketRepository.findById(flight.getRocketId())
                .orElseThrow(() -> new NotFoundException("Rocket not found: " + flight.getRocketId()));

        // Calculate final price with discounts
        double finalPrice = calculateFinalPrice(flight, rocket);

        // Process payment
        String transactionId = paymentGateway.processPayment(dto.getPassengerEmail(), finalPrice);

        // Create and save booking
        Booking booking = new Booking(null, dto.getFlightId(), dto.getPassengerEmail(),
                                    finalPrice, transactionId, LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // Update flight
        flight.incrementBookedSeats();

        // Check if flight should be confirmed or sold out
        if (flight.getBookedSeats() >= flight.getMinPassengers() && 
            flight.getStatus() == FlightStatus.SCHEDULED) {
            flight.setStatus(FlightStatus.CONFIRMED);
            // Notify all passengers
            List<String> passengerEmails = bookingRepository.findByFlightId(flight.getId())
                    .stream()
                    .map(Booking::getPassengerEmail)
                    .collect(Collectors.toList());
            notificationService.sendFlightConfirmedNotification(flight, passengerEmails);
        }

        if (flight.getBookedSeats() >= rocket.getCapacity()) {
            flight.setStatus(FlightStatus.SOLD_OUT);
        }

        flightRepository.update(flight);

        // Send booking confirmation
        notificationService.sendBookingConfirmation(dto.getPassengerEmail(), flight.getId(), finalPrice);

        return booking;
    }

    private void validateBookingBusinessRules(Flight flight) {
        if (flight.getStatus() == FlightStatus.CANCELLED) {
            throw new BusinessException("Cannot book on a cancelled flight");
        }
        if (flight.getStatus() == FlightStatus.SOLD_OUT) {
            throw new BusinessException("Flight is sold out");
        }
    }

    private double calculateFinalPrice(Flight flight, Rocket rocket) {
        double basePrice = flight.getBasePrice();
        double discountPercentage = 0;

        LocalDateTime now = LocalDateTime.now();
        long daysUntilFlight = ChronoUnit.DAYS.between(now, flight.getDepartureDate());
        long monthsUntilFlight = ChronoUnit.MONTHS.between(now, flight.getDepartureDate());
        
        int remainingSeats = rocket.getCapacity() - flight.getBookedSeats();
        int seatsNeededForMinimum = flight.getMinPassengers() - flight.getBookedSeats();

        // Apply discount rules in order of precedence
        if (remainingSeats == 1) {
            // Last seat - no discount
            discountPercentage = 0;
        } else if (seatsNeededForMinimum == 1) {
            // One seat needed to reach minimum - 30% discount
            discountPercentage = 0.30;
        } else if (monthsUntilFlight > 6) {
            // More than 6 months before - 10% discount
            discountPercentage = 0.10;
        } else if (daysUntilFlight >= 7 && daysUntilFlight <= 30) {
            // Between 1 week and 1 month - 20% discount
            discountPercentage = 0.20;
        }

        return basePrice * (1 - discountPercentage);
    }
}