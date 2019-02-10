package de.hsrm.lback.myapplication.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.JourneyRepository;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;

/**
 * controls a list of journeys
 */
public class JourneyViewModel extends AndroidViewModel {
    private Location src;
    private Location target;
    private LiveData<List<Journey>> journeys;
    private LiveData<Location> srcData;
    private LiveData<Location> targetData;

    public JourneyViewModel(Application application) {
        super(application);
    }

    public void init(int srcId, int targetId) {
        LocationRepository locationRepository = new LocationRepository(getApplication());
        srcData = locationRepository.get(srcId);
        targetData = locationRepository.get(targetId);
        journeys = new MutableLiveData<>();
    }

    /** set target */
    public void onTargetChange(Location location) {
        this.target = location;
        fetchJourneys();
    }

    /** set src */
    public void onSrcChange(Location location) {
        this.src = location;
        fetchJourneys();
    }

    /**
     * load journey data if src and target location are both loaded
     * and set change-listener
     */
    private void fetchJourneys() {
        if (src != null && target != null) {
            JourneyRepository.getAllJourneys(src, target, (MutableLiveData<List<Journey>>) journeys);
        }
    }

    public LiveData<Location> getSrcData() {
        return srcData;
    }

    public LiveData<Location> getTargetData() {
        return targetData;
    }

    public LiveData<List<Journey>> getJourneys() {
        return journeys;
    }
}
