package com.astrobookings.domain.ports;

import com.astrobookings.domain.models.Rocket;
import java.util.List;
import java.util.Optional;

public interface RocketRepositoryPort {
    List<Rocket> findAll();
    Optional<Rocket> findById(String id);
    Rocket save(Rocket rocket);
}