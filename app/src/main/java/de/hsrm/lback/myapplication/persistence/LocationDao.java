package de.hsrm.lback.myapplication.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.hsrm.lback.myapplication.models.Location;

@Dao
public interface LocationDao {
    @Query("SELECT * from location")
    LiveData<List<Location>> getAll();

    @Insert
    void insert(Location location);

    @Update
    void update(Location location);

    @Query("SELECT * from location WHERE uid=:uid")
    LiveData<Location> get(int uid);
}