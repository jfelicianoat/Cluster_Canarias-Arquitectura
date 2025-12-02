package com.astrobookings;

import com.astrobookings.infrastructure.config.DependencyFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Main application class following Hexagonal Architecture pattern.
 * The infrastructure layer (this class) initializes and configures the application.
 */
public class AstroBookingsHexagonalApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            // Create dependency factory for dependency injection
            DependencyFactory factory = new DependencyFactory();

            // Create HTTP server (infrastructure concern)
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Register HTTP handlers (driving adapters)
            server.createContext("/rockets", factory.createRocketHandler());
            server.createContext("/flights", factory.createFlightHandler());
            server.createContext("/bookings", factory.createBookingHandler());
            server.createContext("/admin/cancel-flights", factory.createAdminHandler());

            // Configure thread pool for concurrent requests
            server.setExecutor(Executors.newFixedThreadPool(10));

            // Start the server
            server.start();
            
            printStartupMessage();

        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printStartupMessage() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   🚀 AstroBookings Hexagonal Architecture   ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║                                              ║");
        System.out.println("║  Server running on http://localhost:" + PORT + "    ║");
        System.out.println("║                                              ║");
        System.out.println("║  Architecture Layers:                        ║");
        System.out.println("║  • Domain (Core Business Logic)              ║");
        System.out.println("║  • Application (Use Cases)                   ║");
        System.out.println("║  • Infrastructure (Adapters)                 ║");
        System.out.println("║                                              ║");
        System.out.println("║  Available Endpoints:                        ║");
        System.out.println("║  • GET/POST /rockets                         ║");
        System.out.println("║  • GET/POST /flights                         ║");
        System.out.println("║  • GET/POST /bookings                        ║");
        System.out.println("║  • POST     /admin/cancel-flights           ║");
        System.out.println("║                                              ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("\nPress Ctrl+C to stop the server");
    }
}