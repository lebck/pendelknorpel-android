package de.hsrm.lback.myapplication.services;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import de.hsrm.lback.myapplication.domains.location.models.Location;
import de.hsrm.lback.myapplication.helpers.tasks.GetLocationTask;
import de.hsrm.lback.myapplication.helpers.tasks.GpsTask;
import de.hsrm.lback.myapplication.helpers.tasks.LocationAsyncTask;
import de.hsrm.lback.myapplication.helpers.tasks.SearchLocationsTask;
import de.hsrm.lback.myapplication.persistence.AppDatabase;
import de.hsrm.lback.myapplication.persistence.LocationDao;

/** Used to retrieve Location objects */
public class LocationService {
    private LocationDao locationDao;
    private LiveData<List<Location>> allLocations;

    public LocationService(Context context) {
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

    public void get(int uid, MutableLiveData<Location> target) {
        new GetLocationTask(target, locationDao, uid).execute();
    }

    public LiveData<Location> get(int uid) {
        MutableLiveData<Location> liveData = new MutableLiveData<>();

        new GetLocationTask(liveData, locationDao, uid).execute();

        return liveData;
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

    public LiveData<List<Location>> getLocationsForGpsCoords(double lat, double lon) {
        MutableLiveData<List<Location>> locationsLiveData = new MutableLiveData<>();

        new GpsTask(locationsLiveData).execute(lat, lon);

        return locationsLiveData;
    }
}
