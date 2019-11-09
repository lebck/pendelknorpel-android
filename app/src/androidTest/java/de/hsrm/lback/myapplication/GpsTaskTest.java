package de.hsrm.lback.myapplication;

import android.arch.lifecycle.MutableLiveData;

import org.junit.Test;

import java.util.List;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.helpers.tasks.GpsTask;

public class GpsTaskTest {

    @Test
    public void shouldFillLiveDataWithLocationList() {

        double lat = 50.078793;
        double lon = 8.229426;

        MutableLiveData<List<Location>> liveData = new MutableLiveData<>();

        liveData.observe(appContext, locations -> {
            assert locations != null;
            assert locations.size() > 0;
        });

        GpsTask gpsTask = new GpsTask(liveData);

        gpsTask.execute(lat, lon);
    }
}
