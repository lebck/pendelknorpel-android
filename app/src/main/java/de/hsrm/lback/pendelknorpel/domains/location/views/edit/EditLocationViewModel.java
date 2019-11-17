package de.hsrm.lback.pendelknorpel.domains.location.views.edit;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.text.Editable;

import java.util.Collections;
import java.util.List;

import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.domains.location.views.edit.components.search.SearchLocationViewModel;
import de.hsrm.lback.pendelknorpel.services.LocationService;

public class EditLocationViewModel extends AndroidViewModel implements SearchLocationViewModel {
    public static final int ANONYMOUS_UID = -1;
    public static final int NEW_UID = 0;
    private MutableLiveData<List<Location>> locationResults;
    private LocationService locationService;
    private MutableLiveData<Location> locationData;

    public EditLocationViewModel(Application application, int locationUid) {
        super(application);
        this.locationResults = new MutableLiveData<>();
        this.locationResults.setValue(Collections.emptyList());
        this.locationService = new LocationService(application.getApplicationContext());
        this.locationData = initializeLocationData(locationUid);
    }

    private MutableLiveData<Location> initializeLocationData(int uid) {
        MutableLiveData<Location> locationData = new MutableLiveData<>();

        // if location already exists and is not anonymous => exists in database
        if (uid != NEW_UID && uid != ANONYMOUS_UID) {
            locationService.get(uid, locationData);
        } else {
            locationData.setValue(new Location(""));
        }

        return locationData;
    }

    public void search(String searchTerm) {
        this.locationService.search(searchTerm, this.locationResults);
    }

    public boolean update() {
        if (this.locationData.getValue() != null) {
            int uid = this.locationData.getValue().getUid();

            if (uid == ANONYMOUS_UID) return false;

            if (this.locationData.getValue().getUid() == 0) {
                locationService.insert(this.locationData.getValue());
            } else {
                locationService.update(this.locationData.getValue());
            }
            return true;
        }

        return false;
    }

    public void delete() {
        locationService.delete(this.getLocation());
        this.locationData.setValue(null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchTerm = s.toString();

        this.search(searchTerm);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals("")) this.locationResults.setValue(Collections.emptyList());
    }

    public LiveData<List<Location>> getLocationResults() {
        return locationResults;
    }

    public void setDisplayName(String displayName) {
        this.getLocation().setDisplayName(displayName);
        this.locationData.postValue(this.getLocation());
    }

    public void setName(String name) {
        this.getLocation().setName(name);
        this.locationData.postValue(this.getLocation());
    }

    public void setLocationByIndex(int index) {
        Location currentLocation = locationResults.getValue().get(index);

        currentLocation.setDisplayName(getLocation().getDisplayName());
        currentLocation.setLogo(getLocation().getLogo());
        currentLocation.setUid(getLocation().getUid());

        locationData.setValue(currentLocation);
    }

    public Location getLocation() {
        return locationData.getValue();
    }

    public void setLocation(Location location) {
        this.locationData.postValue(location);
    }

    public void setLogo(String s) {
        this.getLocation().setLogo(s);
        this.locationData.postValue(this.getLocation());
    }

    public MutableLiveData<Location> getLocationData() {
        return locationData;
    }

    @Override
    public void cleanUp() {
        this.locationData.setValue(new Location(""));
        this.locationResults.setValue(Collections.emptyList());
    }

    public static class Factory implements ViewModelProvider.Factory {
        private int locationUid;
        private Application application;

        public Factory(Application application, int locationUid) {
            this.locationUid = locationUid;
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new EditLocationViewModel(application, locationUid);
        }
    }
}
