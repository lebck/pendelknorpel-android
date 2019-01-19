package de.hsrm.lback.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import de.hsrm.lback.myapplication.network.ApiConnector;
import de.hsrm.lback.myapplication.models.Location;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("de.hsrm.lback.myapplication", appContext.getPackageName());
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
