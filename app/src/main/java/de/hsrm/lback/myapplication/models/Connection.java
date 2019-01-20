package de.hsrm.lback.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Connection {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String lineId;
    private String vehicle;
    private Location startLocation;
    private Location endLocation;

    public Connection(Location startLocation, Location endLocation, LocalDateTime startTime, LocalDateTime endTime, String lineId, String vehicle) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.lineId = lineId;
        this.vehicle = vehicle;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public Connection() {}

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStartTimeString() {
        return startTime.format(FORMATTER);
    }

    public String getEndTimeString() {
        return endTime.format(FORMATTER);
    }

    public String getLineId() {
        return lineId;
    }

    public String getVehicle() {
        return vehicle;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }
}
