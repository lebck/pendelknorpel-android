package de.hsrm.lback.myapplication.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.view.DragEvent;
import android.view.View;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.views.views.LocationView;

/**
 * VM of a location
 */
public class LocationViewModel extends AndroidViewModel {

    private Location location;
    private LocationRepository locationRepository;
    private boolean anonymous;
    private boolean gps;

    public LocationViewModel(Application application) {
        super(application);
        this.locationRepository = new LocationRepository(application);
        this.anonymous = false;
        this.gps = false;
    }

    public void init(Location location) {
        this.location = location;

    }

    public void update() {
        if (this.location.getUid() == 0)
            locationRepository.insert(this.location);
        else
            locationRepository.update(this.location);
    }

    public void delete() {
        locationRepository.delete(this.location);
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }
}
