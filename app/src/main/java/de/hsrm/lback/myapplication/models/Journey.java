package de.hsrm.lback.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Journey implements Parcelable {
    private Location srcLocation;
    private Location targetLocation;
    private List<Connection> connections;


    public Journey(Location srcLocation, Location targetLocation, List<Connection> connections) {
        this.srcLocation = srcLocation;
        this.targetLocation = targetLocation;
        this.connections = connections;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(srcLocation, flags);
        dest.writeParcelable(targetLocation, flags);
        dest.writeList(connections);
    }

    protected Journey(Parcel in) {
        srcLocation = in.readParcelable(Location.class.getClassLoader());
        targetLocation = in.readParcelable(Location.class.getClassLoader());
        connections = new ArrayList<>();
        in.readList(connections, Connection.class.getClassLoader());
    }

    public static final Creator<Journey> CREATOR = new Creator<Journey>() {
        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey(in);
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };

    public Location getSrcLocation() {
        return srcLocation;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public String getVehicleString() {
        List <String> vehicles = new ArrayList<>();
        for(Connection c : connections) {
            vehicles.add(c.getVehicle().toString());
        }

        return String.join(", ", vehicles);
    }

    @Override
    public String toString() {
        return String.format("Journey(%s, %s, %s)", srcLocation.toString(), targetLocation.toString(), connections.toString());
    }
}
