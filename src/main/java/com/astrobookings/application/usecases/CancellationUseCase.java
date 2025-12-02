package com.astrobookings.application.usecases;

import com.astrobookings.application.ports.CancellationUseCasePort;
import com.astrobookings.domain.services.CancellationDomainService;

public class CancellationUseCase implements CancellationUseCasePort {
    private final CancellationDomainService cancellationDomainService;

    public CancellationUseCase(CancellationDomainService cancellationDomainService) {
        this.cancellationDomainService = cancellationDomainService;
    }

    @Override
    public int processCancellations() {
        return cancellationDomainService.processCancellations();
    }
}