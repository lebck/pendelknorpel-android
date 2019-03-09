package de.hsrm.lback.myapplication.models.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.tasks.LocationAsyncTask;
import de.hsrm.lback.myapplication.network.SearchLocationsTask;
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
        new LocationAsyncTask(locations -> locationDao.insert(locations[0])).execute(location);
    }

    public LiveData<Location> get(int uid) {
        return locationDao.get(uid);
    }

    public void update(Location location) {
        new LocationAsyncTask(locations -> locationDao.update(locations[0])).execute(location);
    }

    public void delete(Location location) {
        new LocationAsyncTask(locations -> locationDao.delete(locations[0])).execute(location);
    }

    public void search(String searchTerm, MutableLiveData<List<Location>> targetList) {
        if (searchTerm.length() > 3)
            new SearchLocationsTask(targetList).execute(searchTerm);
        else
            targetList.setValue(Collections.emptyList());
    }

    public static Location getLocationByJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Location.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeLocation(Location location) {
        try {
            return new ObjectMapper().writeValueAsString(location);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
