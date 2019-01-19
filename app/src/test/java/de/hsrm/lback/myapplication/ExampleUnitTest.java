package de.hsrm.lback.myapplication;

import org.junit.Test;

import java.io.IOException;

import de.hsrm.lback.myapplication.network.ApiConnector;
import de.hsrm.lback.myapplication.models.Location;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testApiConnector() throws IOException {
        Location wiesbaden = new Location("Wiesbaden hbf", 0);
        Location wien = new Location("Wien hbf", 0);

        wiesbaden.setApiId(8000250);
        wien.setApiId(8103000);

        new ApiConnector().getDepartures(wiesbaden, wien);

    }
}