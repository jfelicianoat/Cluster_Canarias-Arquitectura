package com.astrobookings.business;

import com.astrobookings.business.dto.CreateRocketDto;
import com.astrobookings.business.exceptions.ValidationException;
import com.astrobookings.persistence.RocketRepository;
import com.astrobookings.persistence.models.Rocket;

import java.util.List;

public class RocketService {
    private final RocketRepository rocketRepository;

    public RocketService(RocketRepository rocketRepository) {
        this.rocketRepository = rocketRepository;
    }

    public List<Rocket> getAllRockets() {
        return rocketRepository.findAll();
    }

    public Rocket createRocket(CreateRocketDto dto) {
        // Validate structure (DTO validation)
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ValidationException("Rocket name must be provided");
        }
        if (dto.getCapacity() == null) {
            throw new ValidationException("Rocket capacity must be provided");
        }

        // Business validation
        validateRocketBusinessRules(dto);

        Rocket rocket = new Rocket(null, dto.getName(), dto.getCapacity(), dto.getSpeed());
        return rocketRepository.save(rocket);
    }

    private void validateRocketBusinessRules(CreateRocketDto dto) {
        // Business rule: capacity must be between 1 and 10
        if (dto.getCapacity() < 1) {
            throw new ValidationException("Rocket capacity must be at least 1");
        }
        if (dto.getCapacity() > 10) {
            throw new ValidationException("Rocket capacity cannot exceed 10");
        }
    }
}