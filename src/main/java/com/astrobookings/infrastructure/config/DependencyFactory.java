package com.astrobookings.infrastructure.config;

import com.astrobookings.application.ports.*;
import com.astrobookings.application.usecases.*;
import com.astrobookings.domain.ports.*;
import com.astrobookings.domain.services.*;
import com.astrobookings.infrastructure.persistence.adapters.*;
import com.astrobookings.infrastructure.presentation.adapters.*;
import com.sun.net.httpserver.HttpHandler;

/**
 * Factory for dependency injection following the hexagonal architecture pattern.
 * This class is responsible for creating and wiring all the components.
 */
public class DependencyFactory {
    
    // Domain ports (repositories and external services)
    private final RocketRepositoryPort rocketRepository;
    private final FlightRepositoryPort flightRepository;
    private final BookingRepositoryPort bookingRepository;
    private final PaymentGatewayPort paymentGateway;
    private final NotificationServicePort notificationService;
    
    // Domain services
    private final RocketDomainService rocketDomainService;
    private final FlightDomainService flightDomainService;
    private final BookingDomainService bookingDomainService;
    private final CancellationDomainService cancellationDomainService;
    
    // Application use cases
    private final RocketUseCasePort rocketUseCase;
    private final FlightUseCasePort flightUseCase;
    private final BookingUseCasePort bookingUseCase;
    private final CancellationUseCasePort cancellationUseCase;
    
    public DependencyFactory() {
        // Initialize persistence adapters
        this.rocketRepository = new RocketRepositoryAdapter();
        this.flightRepository = new FlightRepositoryAdapter();
        this.bookingRepository = new BookingRepositoryAdapter();
        this.paymentGateway = new PaymentGatewayAdapter();
        this.notificationService = new NotificationServiceAdapter();
        
        // Initialize domain services with their dependencies
        this.rocketDomainService = new RocketDomainService(rocketRepository);
        this.flightDomainService = new FlightDomainService(flightRepository, rocketRepository);
        this.bookingDomainService = new BookingDomainService(
            bookingRepository,
            flightRepository,
            rocketRepository,
            paymentGateway,
            notificationService
        );
        this.cancellationDomainService = new CancellationDomainService(
            flightRepository,
            bookingRepository,
            paymentGateway,
            notificationService
        );
        
        // Initialize application use cases
        this.rocketUseCase = new RocketUseCase(rocketDomainService);
        this.flightUseCase = new FlightUseCase(flightDomainService);
        this.bookingUseCase = new BookingUseCase(bookingDomainService);
        this.cancellationUseCase = new CancellationUseCase(cancellationDomainService);
    }
    
    // Factory methods for HTTP handlers
    public HttpHandler createRocketHandler() {
        return new RocketHandler(rocketUseCase);
    }
    
    public HttpHandler createFlightHandler() {
        return new FlightHandler(flightUseCase);
    }
    
    public HttpHandler createBookingHandler() {
        return new BookingHandler(bookingUseCase);
    }
    
    public HttpHandler createAdminHandler() {
        return new AdminHandler(cancellationUseCase);
    }
}