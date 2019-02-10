package de.hsrm.lback.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * a single connection between two locations connected by one bus/train line
 */
public class Connection {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter JSON_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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

    @JsonIgnore
    public LocalDateTime getStartTimeObject() {
        return startTime;
    }

    @JsonIgnore
    public LocalDateTime getEndTimeObject() {
        return endTime;
    }

    public String getStartTime() {
        return startTime.toString();
    }

    public String getEndTime() {
        return endTime.toString();
    }
    @JsonIgnore
    public String getStartTimeString() {
        return startTime.format(FORMATTER);
    }

    @JsonIgnore
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

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = LocalDateTime.parse(endTime);
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

    @Override
    public String toString() {
        return "Connection{" +
                "lineId='" + lineId + '\'' +
                ", vehicle='" + vehicle + '\'' +
                ", startLocation=" + startLocation +
                ", endLocation=" + endLocation +
                '}';
    }

    public void setStartTimeObject(LocalDateTime startTimeObject) {
        this.startTime = startTimeObject;
    }

    public void setEndTimeObject(LocalDateTime endDateTime) {
        this.endTime = endDateTime;
    }
}
