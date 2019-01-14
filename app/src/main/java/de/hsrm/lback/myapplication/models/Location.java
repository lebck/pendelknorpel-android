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
public class Location implements Parcelable {
    public static final String LOCATION_UID = "location_uid";
    public static final String SRC_LOCATION = "start_location";
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
        init(name, position, logoName);
    }

    public Location(String name, int position) {
        this.position = position;

        this.logo = new MutableLiveData<>();
        this.logo.setValue(DEFAULT_LOGO_NAME);

        this.name = new MutableLiveData<>();
        this.name.setValue(name);
    }

    private void init(String name, int position, String logoName) {
        this.position = position;

        this.logo = new MutableLiveData<>();
        this.logo.setValue(logoName);

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

    @Override
    public String toString() {
        return String.format("Location{ %s, %s, %s }", name.getValue(), position, uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeInt(position);
        dest.writeString(name.getValue());
        dest.writeString(logo.getValue());

    }

    protected Location(Parcel in) {
        int uid = in.readInt();
        int position = in.readInt();
        String name = in.readString();
        String logo = in.readString();

        init(name, position, logo);

        this.uid = uid;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

}
