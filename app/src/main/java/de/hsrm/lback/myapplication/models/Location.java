package de.hsrm.lback.myapplication.models;

public class Location {
    private String name;
    private int position;

    public Location(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }
}
