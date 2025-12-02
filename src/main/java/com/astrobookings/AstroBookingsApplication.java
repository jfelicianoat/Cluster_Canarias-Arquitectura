package com.astrobookings;

import com.astrobookings.business.*;
import com.astrobookings.persistence.*;
import com.astrobookings.presentation.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class AstroBookingsApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            // Initialize repositories
            RocketRepository rocketRepository = new RocketRepository();
            FlightRepository flightRepository = new FlightRepository();
            BookingRepository bookingRepository = new BookingRepository();

            // Initialize gateways and notification service
            PaymentGateway paymentGateway = new PaymentGateway();
            NotificationService notificationService = new NotificationService();

            // Initialize services
            RocketService rocketService = new RocketService(rocketRepository);
            FlightService flightService = new FlightService(flightRepository, rocketRepository);
            BookingService bookingService = new BookingService(
                    bookingRepository,
                    flightRepository,
                    rocketRepository,
                    paymentGateway,
                    notificationService
            );
            CancellationService cancellationService = new CancellationService(
                    flightRepository,
                    bookingRepository,
                    paymentGateway,
                    notificationService
            );

            // Create HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Create and register handlers
            server.createContext("/rockets", new RocketHandler(rocketService));
            server.createContext("/flights", new FlightHandler(flightService));
            server.createContext("/bookings", new BookingHandler(bookingService));
            server.createContext("/admin/cancel-flights", new AdminHandler(cancellationService));

            // Set executor (optional but recommended for production)
            server.setExecutor(Executors.newFixedThreadPool(10));

            // Start server
            server.start();
            
            System.out.println("================================");
            System.out.println("🚀 AstroBookings Server Started");
            System.out.println("================================");
            System.out.println("Server running on http://localhost:" + PORT);
            System.out.println("Available endpoints:");
            System.out.println("  GET/POST /rockets");
            System.out.println("  GET/POST /flights");
            System.out.println("  GET/POST /bookings");
            System.out.println("  POST     /admin/cancel-flights");
            System.out.println("================================");
            System.out.println("Press Ctrl+C to stop the server");

        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}