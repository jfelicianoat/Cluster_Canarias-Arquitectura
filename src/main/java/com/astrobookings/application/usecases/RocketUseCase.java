package com.astrobookings.application.usecases;

import com.astrobookings.application.ports.RocketUseCasePort;
import com.astrobookings.business.dto.CreateRocketDto;
import com.astrobookings.domain.models.Rocket;
import com.astrobookings.domain.services.RocketDomainService;
import java.util.List;

public class RocketUseCase implements RocketUseCasePort {
    private final RocketDomainService rocketDomainService;

    public RocketUseCase(RocketDomainService rocketDomainService) {
        this.rocketDomainService = rocketDomainService;
    }

    @Override
    public List<Rocket> getAllRockets() {
        return rocketDomainService.getAllRockets();
    }

    @Override
    public Rocket createRocket(CreateRocketDto dto) {
        return rocketDomainService.createRocket(dto);
    }
}