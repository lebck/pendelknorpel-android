package de.hsrm.lback.myapplication.models;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import org.jetbrains.annotations.Nullable;

import de.hsrm.lback.myapplication.persistence.StringLiveDataConverter;


/**
 * Representation of a Location with coordinate and logo
 */
@Entity
@TypeConverters({StringLiveDataConverter.class})
public class Location {
    public static final String LOCATION_UID = "location_uid";
    public static final String START_LOCATION = "start_location";
    public static final String DESTINATION_LOCATION = "destination_location";
    private static final String DEFAULT_LOGO_NAME = "ic_add_black_24dp";

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private MutableLiveData<String> name;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(name = "logo")
    private MutableLiveData<String> logo;

    /** constructor used by rooms */
    public Location(MutableLiveData<String> name, int position, MutableLiveData<String> logo) {
        this.position = position;
        this.logo = logo;
        this.name = name;
    }

    public Location(String name, int position, String logoName) {
        this.position = position;

        this.logo = new MutableLiveData<>();
        this.logo.setValue(logoName);

        this.name = new MutableLiveData<>();
        this.name.setValue(name);
    }

    public Location(String name, int position) {
        this.position = position;

        this.logo = new MutableLiveData<>();
        this.logo.setValue(DEFAULT_LOGO_NAME);

        this.name = new MutableLiveData<>();
        this.name.setValue(name);
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

    public MutableLiveData<String> getLogo() {
        return logo;
    }
}
