package de.hsrm.lback.myapplication.models;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

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
    public static final String SERIALIZED_LOCATION = "serialized_location";
    private static final String DEFAULT_LOGO_NAME = "plus";

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "logo")
    private String logo;

    @ColumnInfo(name = "apiId")
    private String apiId;

    @ColumnInfo(name = "displayName")
    private String displayName;

    /** constructor used by rooms */
    public Location(String name, String logo, String displayName) {
        this.logo = logo;
        this.name = name;
        this.displayName = displayName;
    }

    @Ignore // ignored by rooms
    public Location() {}

    @Ignore // ignored by rooms
    public Location(String name) {

        this.logo = DEFAULT_LOGO_NAME;

        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Location{ %s, %s, %s }", name, uid, apiId);
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {

        this.name = name.trim();
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLogo() {
        return logo;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public void setPosition(int position) {
    }


    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
