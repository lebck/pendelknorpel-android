package de.hsrm.lback.myapplication.models;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import de.hsrm.lback.myapplication.persistence.converters.StringLiveDataConverter;


/**
 * Representation of a Location with coordinate and logo
 */
@Entity
@TypeConverters({StringLiveDataConverter.class})
public class Location {
    public static final String LOCATION_UID = "location_uid";
    public static final String SRC_LOCATION = "start_location";
    public static final String DESTINATION_LOCATION = "destination_location";
    private static final String DEFAULT_LOGO_NAME = "plus";

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private MutableLiveData<String> name;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(name = "logo")
    private MutableLiveData<String> logo;

    @ColumnInfo(name = "apiId")
    private int apiId;

    /** constructor used by rooms */
    public Location(MutableLiveData<String> name, int position, MutableLiveData<String> logo) {
        this.position = position;
        this.logo = logo;
        this.name = name;
    }

    public Location() {}

    public Location(String name, int position, String logoName) {
        init(name, position, logoName);
    }

    public Location(String name, int position) {
        this.position = position;

        this.logo = new MutableLiveData<>();
        this.logo.postValue(DEFAULT_LOGO_NAME);

        this.name = new MutableLiveData<>();
        this.name.postValue(name);
    }

    private void init(String name, int position, String logoName) {
        this.position = position;

        this.logo = new MutableLiveData<>();
        this.logo.setValue(logoName);

        this.name = new MutableLiveData<>();
        this.name.setValue(name);
    }

    @Override
    public String toString() {
        return String.format("Location{ %s, %s, %s }", name.getValue(), position, uid);
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

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public void setName(MutableLiveData<String> name) {
        this.name = name;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLogo(MutableLiveData<String> logo) {
        this.logo = logo;
    }
}
