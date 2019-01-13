package de.hsrm.lback.myapplication.models.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.tasks.InsertAsyncTask;
import de.hsrm.lback.myapplication.models.repositories.tasks.UpdateAsyncTask;
import de.hsrm.lback.myapplication.persistence.AppDatabase;
import de.hsrm.lback.myapplication.persistence.LocationDao;

/** Used to retrieve Location objects */
public class LocationRepository {
    private LocationDao locationDao;
    private LiveData<List<Location>> allLocations;

    public LocationRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);

        locationDao = db.locationDao();

        allLocations = locationDao.getAll();
    }

    public LiveData<List<Location>> getAllLocations() {
        return allLocations;
    }

    public void insert(Location location) {
        new InsertAsyncTask(locationDao).execute(location);
    }

    public void update(Location location) {
        new UpdateAsyncTask(locationDao).execute(location);
    }

    public LiveData<Location> get(int uid) {
        return locationDao.get(uid);
    }

}
