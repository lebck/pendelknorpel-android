package de.hsrm.lback.myapplication.models;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import de.hsrm.lback.myapplication.persistence.NameConverter;


/**
 * Representation of a Location with coordinate
 */
@Entity
@TypeConverters({NameConverter.class})
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private MutableLiveData<String> name;

    @ColumnInfo(name = "position")
    private int position;

    public Location(MutableLiveData<String> name, int position) {
        this(position);

        this.name = name;
    }

    public Location(String name, int position) {
        this(position);

        this.name = new MutableLiveData<>();
        this.name.setValue(name);
    }

    private Location(int position) {
        this.position = position;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public boolean setName(String name) {
        if (name != null && !name.equals("")) {
            this.name.setValue(name);
            return true;
        } else {
          return false;
        }
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
