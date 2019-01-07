package de.hsrm.lback.myapplication.models.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hsrm.lback.myapplication.models.Location;
/** Used to retrieve Location objects */
public class LocationRepository {
    public static List<Location> getLocations() {
        return new ArrayList<>(
                Arrays.asList(
                        new Location("Wsb HBF", 0),
                        new Location("GB Mainstr", 0),
                        new Location("Wsb HBF", 0),
                        new Location("Wsb HBF", 0),
                        new Location("Wsb HBF", 0),
                        new Location("Wsb HBF", 0)
                )
        );
    }
}
