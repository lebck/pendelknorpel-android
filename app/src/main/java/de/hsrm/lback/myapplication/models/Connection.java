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

    public Connection(LocalDateTime startTime, LocalDateTime endTime, String lineId, String vehicle) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.lineId = lineId;
        this.vehicle = vehicle;
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
    }

    protected Connection(Parcel in) {
        startTime = (LocalDateTime) in.readSerializable();
        endTime = (LocalDateTime) in.readSerializable();
        lineId = in.readString();
        vehicle = in.readString();
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

    public String getLineId() {
        return lineId;
    }

    public String getVehicle() {
        return vehicle;
    }

}
