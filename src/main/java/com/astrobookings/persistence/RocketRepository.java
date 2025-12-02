package com.astrobookings.persistence;

import com.astrobookings.persistence.models.Rocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RocketRepository {
    private final Map<String, Rocket> rockets = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public RocketRepository() {
        // Pre-load initial rocket
        Rocket falcon9 = new Rocket("r1", "Falcon 9", 10, 28000.0);
        rockets.put(falcon9.getId(), falcon9);
        idCounter.incrementAndGet();
    }

    public List<Rocket> findAll() {
        return new ArrayList<>(rockets.values());
    }

    public Optional<Rocket> findById(String id) {
        return Optional.ofNullable(rockets.get(id));
    }

    public Rocket save(Rocket rocket) {
        String id = rocket.getId() != null ? rocket.getId() : "r" + idCounter.getAndIncrement();
        Rocket rocketToSave = new Rocket(id, rocket.getName(), rocket.getCapacity(), rocket.getSpeed());
        rockets.put(id, rocketToSave);
        return rocketToSave;
    }
}