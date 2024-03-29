package de.hsrm.lback.pendelknorpel.domains.location.tasks;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.persistence.LocationDao;

public class GetLocationTask extends AsyncTask<Void, Void, Void> {
    private MutableLiveData<Location> locationData;
    private LocationDao locationDao;
    private int uid;

    public GetLocationTask(MutableLiveData<Location> locationData, LocationDao locationDao, int uid) {
        this.locationData = locationData;
        this.locationDao = locationDao;
        this.uid = uid;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Location location = locationDao.get(uid);

        locationData.postValue(location);

        return null;
    }
}
