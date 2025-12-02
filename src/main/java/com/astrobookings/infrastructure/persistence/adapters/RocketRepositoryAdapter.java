package com.astrobookings.infrastructure.persistence.adapters;

import com.astrobookings.domain.models.Rocket;
import com.astrobookings.domain.ports.RocketRepositoryPort;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RocketRepositoryAdapter implements RocketRepositoryPort {
    private final Map<String, Rocket> rockets = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public RocketRepositoryAdapter() {
        // Pre-load initial rocket
        Rocket falcon9 = new Rocket("r1", "Falcon 9", 10, 28000.0);
        rockets.put(falcon9.getId(), falcon9);
        idCounter.incrementAndGet();
    }

    @Override
    public List<Rocket> findAll() {
        return new ArrayList<>(rockets.values());
    }

    @Override
    public Optional<Rocket> findById(String id) {
        return Optional.ofNullable(rockets.get(id));
    }

    @Override
    public Rocket save(Rocket rocket) {
        String id = rocket.getId() != null ? rocket.getId() : "r" + idCounter.getAndIncrement();
        Rocket rocketToSave = new Rocket(id, rocket.getName(), rocket.getCapacity(), rocket.getSpeed());
        rockets.put(id, rocketToSave);
        return rocketToSave;
    }
}