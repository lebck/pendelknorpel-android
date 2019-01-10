package de.hsrm.lback.myapplication.models.repositories.tasks;

import android.os.AsyncTask;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.persistence.LocationDao;

public abstract class LocationAsyncTask extends AsyncTask<Location, Void, Void> {

    protected LocationDao locationDao;

    public LocationAsyncTask(LocationDao locationDao) {
        this.locationDao = locationDao;
    }
}
