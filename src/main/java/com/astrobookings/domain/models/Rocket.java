package com.astrobookings.domain.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rocket {
    private final String id;
    private final String name;
    private final int capacity;
    private final Double speed;

    @JsonCreator
    public Rocket(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("capacity") int capacity,
            @JsonProperty("speed") Double speed) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public Double getSpeed() {
        return speed;
    }
}