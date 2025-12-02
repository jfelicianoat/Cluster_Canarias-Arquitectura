package com.astrobookings.application.ports;

import com.astrobookings.business.dto.CreateRocketDto;
import com.astrobookings.domain.models.Rocket;
import java.util.List;

public interface RocketUseCasePort {
    List<Rocket> getAllRockets();
    Rocket createRocket(CreateRocketDto dto);
}