package de.hsrm.lback.myapplication.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Connection {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String lineId;
    private Vehicle vehicle;

    public Connection(LocalDateTime startTime, LocalDateTime endTime, String lineId, Vehicle vehicle) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.lineId = lineId;
        this.vehicle = vehicle;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getLineId() {
        return lineId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
