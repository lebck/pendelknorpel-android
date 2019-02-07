package de.hsrm.lback.myapplication;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.network.ApiConnector;

public class RmvApiTest {
    @Test
    public void testRmvApi() {
        List<Location> locations = new ApiConnector().getLocationsBySearchTerm("Wiesbaden");

        System.out.println(locations);
    }

    @Test
    public void testFromToSearch() {
        Location hbf = new Location("Wiesbaden hbf", 0);
        hbf.setApiId("003006907");
        Location platte = new Location("Platte", 0);
        platte.setApiId("003018169");
        List<Journey> journeys = new ApiConnector().getDepartures(hbf, platte, LocalDateTime.now());

        System.out.println(journeys);
    }
}
