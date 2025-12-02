package com.astrobookings.business.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRocketDto {
    private final String name;
    private final Integer capacity;
    private final Double speed;

    @JsonCreator
    public CreateRocketDto(
            @JsonProperty("name") String name,
            @JsonProperty("capacity") Integer capacity,
            @JsonProperty("speed") Double speed) {
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Double getSpeed() {
        return speed;
    }
}