package de.hsrm.lback.myapplication.models;

import android.arch.lifecycle.MutableLiveData;

/**
 * Representation of a Location with coordinate
 */
public class Location {
    private MutableLiveData<String> name;
    private int position;

    public Location(String name, int position) {
        this.name = new MutableLiveData<>();
        this.name.setValue(name);
        this.position = position;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public boolean setName(String name) {
        if (name != null && !name.equals("")) {
            this.name.setValue(name);
            return true;
        } else {
          return false;
        }
    }
}
