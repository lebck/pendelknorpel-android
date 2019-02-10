package de.hsrm.lback.myapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * contains one or more connections
 */
public class Journey {
    public static final String JOURNEY_ID = "journey";
    private Location srcLocation;
    private Location targetLocation;
    private List<Connection> connections;


    public Journey(List<Connection> connections) {
        setConnections(connections);
    }

    public Journey(){}

    public Location getSrcLocation() {
        return srcLocation;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setSrcLocation(Location srcLocation) {
        this.srcLocation = srcLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;

        this.srcLocation = connections.get(0).getStartLocation();
        this.targetLocation = connections.get(connections.size() - 1).getEndLocation();
    }

    @JsonIgnore
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

    @JsonIgnore
    public String getDetailString() {
        return String.format("%s -> %s",
                srcLocation.getName(),
                targetLocation.getName()
        );
    }
}
