package de.hsrm.lback.pendelknorpel.helpers.tasks;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;

import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.network.ApiConnector;

public class GpsTask extends AsyncTask<Double, Void, Void> {
    private MutableLiveData<List<Location>> targetList;

    public GpsTask(MutableLiveData<List<Location>> targetList) {
        this.targetList = targetList;
    }

    @Override
    protected Void doInBackground(Double... floats) {
        double lat = floats[0];
        double lon = floats[1];
        ApiConnector connector = new ApiConnector();
        List<Location> locationList = connector.getLocationsByLatLon(lat, lon);
        targetList.postValue(locationList);

        return null;
    }
}
