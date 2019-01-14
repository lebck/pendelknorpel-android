package de.hsrm.lback.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Connection implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(startTime);
        dest.writeSerializable(endTime);
        dest.writeString(lineId);
        dest.writeString(vehicle);
        dest.writeParcelable(startLocation, flags);
        dest.writeParcelable(endLocation, flags);
    }

    protected Connection(Parcel in) {
        startTime = (LocalDateTime) in.readSerializable();
        endTime = (LocalDateTime) in.readSerializable();
        lineId = in.readString();
        vehicle = in.readString();
        startLocation = in.readParcelable(Location.class.getClassLoader());
        endLocation = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Connection> CREATOR = new Creator<Connection>() {
        @Override
        public Connection createFromParcel(Parcel in) {
            return new Connection(in);
        }

        @Override
        public Connection[] newArray(int size) {
            return new Connection[size];
        }
    };

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
}
