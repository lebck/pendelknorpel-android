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
    private static final String DEFAULT_LOGO_NAME = "ic_android_black_24dp";

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private MutableLiveData<String> name;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(name = "logo")
    private MutableLiveData<String> logo;

    public Location(MutableLiveData<String> name, int position, @Nullable MutableLiveData<String> logo) {
        this(position, null);

        this.logo = logo;
        this.name = name;
    }

    public Location(String name, int position, @Nullable String logoName) {
        this(position, logoName);

        this.name = new MutableLiveData<>();
        this.name.postValue(name);

    }

    private Location(int position, @Nullable String logoName) {
        this.position = position;
        this.logo = new MutableLiveData<>();

        if (logoName == null)
            this.logo.postValue(DEFAULT_LOGO_NAME);
        else
            this.logo.postValue(logoName);
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
