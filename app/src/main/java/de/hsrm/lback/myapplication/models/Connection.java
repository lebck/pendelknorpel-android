package de.hsrm.lback.myapplication.models;

import java.time.LocalDateTime;

public class Connection {
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
