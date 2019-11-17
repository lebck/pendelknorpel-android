package de.hsrm.lback.pendelknorpel.domains.location.views.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import java.util.List;

import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.services.LocationService;

public class LocationOverviewViewModel extends AndroidViewModel {
    public static class AnonymousLocations {

        public boolean isSrcGps = false;
        public boolean isTargetGps = false;
        public Location anonymousSrcLocation;
        public Location anonymousTargetLocation;
    }
    private LocationService locationService;
    private LiveData<List<Location>> locationsData;

    private MutableLiveData<AnonymousLocations> anonymousLocationsData;
    public LocationOverviewViewModel(Application application) {
        super(application);

        this.locationService = new LocationService(application);
        this.anonymousLocationsData = new MutableLiveData<>();

        this.locationsData = locationService.getAllLocations();
    }
    public LiveData<List<Location>> getLocationsData() {
        return locationsData;
    }

    public MutableLiveData<AnonymousLocations> getAnonymousLocationsData() {
        return anonymousLocationsData;
    }

    @Nullable
    private Location findLocation(int uid) {
        List<Location> locations = locationsData.getValue();

        if (locations != null) {
            return locations.stream()
                    .filter(location -> location.getUid() == uid)
                    .findAny()
                    .orElse(null);
        }

        return null;
    }

    public void setAnonymousSrcLocation(Location location, boolean gps) {
        AnonymousLocations locations = this.anonymousLocationsData.getValue();

        if (locations != null) {
            locations.anonymousSrcLocation = location;
        } else {
            locations = new AnonymousLocations();
            locations.anonymousSrcLocation = location;
        }

        locations.isTargetGps = gps;

        this.anonymousLocationsData.postValue(locations);
    }

    public void setAnonymousTargetLocation(Location location, boolean gps) {
        AnonymousLocations locations = this.anonymousLocationsData.getValue();

        if (locations != null) {
            locations.anonymousTargetLocation = location;
        } else {
            locations = new AnonymousLocations();
            locations.anonymousTargetLocation = location;
        }

        locations.isSrcGps = gps;

        this.anonymousLocationsData.postValue(locations);
    }

    public void enableSrcGps() {
        AnonymousLocations locations = this.anonymousLocationsData.getValue();

        if (locations == null) locations = new AnonymousLocations();

        locations.isSrcGps = true;

        this.anonymousLocationsData.setValue(locations);

    }

    public void enableTargetGps() {
        AnonymousLocations locations = this.anonymousLocationsData.getValue();

        if (locations == null) locations = new AnonymousLocations();

        locations.isTargetGps = true;

        this.anonymousLocationsData.setValue(locations);

    }

    public void setAnonymousSrcLocationByUid(int uid) {
        Location location = findLocation(uid);

        setAnonymousSrcLocation(location, false);
    }

    public void setAnonymousTargetLocationByUid(int uid) {
        Location location = findLocation(uid);

        setAnonymousTargetLocation(location, false);
    }

    public void cleanUp() {
        this.anonymousLocationsData.setValue(null);
    }
}
