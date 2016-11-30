package ru.mk.Statistic.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hit {

    @JsonProperty("id")
    private final short counterId;

    @JsonProperty("time")
    private final int duration;

    public Hit(short counterId, int duration) {
        this.counterId = counterId;
        this.duration = duration;
    }

    public short getCounterId() {
        return counterId;
    }

    public int getDuration() {
        return duration;
    }
}
