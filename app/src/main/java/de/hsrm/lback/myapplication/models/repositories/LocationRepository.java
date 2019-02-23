package de.hsrm.lback.myapplication.models.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.tasks.InsertAsyncTask;
import de.hsrm.lback.myapplication.models.repositories.tasks.UpdateAsyncTask;
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
        new InsertAsyncTask(locationDao).execute(location);
    }

    public void update(Location location) {
        new UpdateAsyncTask(locationDao).execute(location);
    }

    public LiveData<Location> get(int uid) {
        return locationDao.get(uid);
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
