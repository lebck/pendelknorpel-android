package de.hsrm.lback.myapplication.models;

import java.util.ArrayList;
import java.util.List;

public class Journey {
    private Location srcLocation;
    private Location targetLocation;
    private List<Connection> connections;


    public Journey(Location srcLocation, Location targetLocation, List<Connection> connections) {
        this.srcLocation = srcLocation;
        this.targetLocation = targetLocation;
        this.connections = connections;
    }

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
}
