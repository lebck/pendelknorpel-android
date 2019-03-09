package de.hsrm.lback.myapplication.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.JourneyRepository;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;

/**
 * controls a list of journeysData
 */
public class JourneyViewModel extends AndroidViewModel {
    private Location src;
    private Location target;
    private LiveData<List<Journey>> journeysData;
    private LiveData<Location> srcData;
    private LiveData<Location> targetData;
    private LocationRepository locationRepository;
    private MutableLiveData<LocalDateTime> dateTimeData;
    private MutableLiveData<Boolean> readyToLoad;

    public JourneyViewModel(Application application) {
        super(application);
    }

    public void init() {
        locationRepository = new LocationRepository(getApplication());
        readyToLoad = new MutableLiveData<>();
        readyToLoad.setValue(false);
        journeysData = new MutableLiveData<>();
        dateTimeData = new MutableLiveData<>();
        dateTimeData.setValue(LocalDateTime.now());
    }

    public void setSrc(int uid) {
        srcData = locationRepository.get(uid);
    }

    public void setSrc(String json) {
        srcData = new MutableLiveData<>();
        ((MutableLiveData<Location>)srcData).setValue(LocationRepository.getLocationByJson(json));
    }

    public void setTarget(int uid) {
        targetData = locationRepository.get(uid);
    }

    public void setTarget(String json) {
        targetData = new MutableLiveData<>();
        ((MutableLiveData<Location>)targetData).setValue(LocationRepository.getLocationByJson(json));
    }

    /** set target */
    public void onTargetChange(Location location) {
        this.target = location;
        updateReadyToLoad();
    }

    /** set src */
    public void onSrcChange(Location location) {
        this.src = location;
        updateReadyToLoad();
    }

    /**
     * load journey data if src and target location are both loaded
     * and set change-listener
     */
    public void fetchJourneys() {
        LocalDateTime dateTime = dateTimeData.getValue();

        // guard to prevent NullPointerException
        if (dateTime == null) dateTime = LocalDateTime.now();

        JourneyRepository.getAllJourneys(
                src,
                target,
                (MutableLiveData<List<Journey>>) journeysData,
                dateTime
        );
    }

    public void onTimeChosen(int hours, int minutes) {
        LocalDateTime dateTime = getDateTimeData().getValue();
        if (dateTime != null) {
            dateTime = dateTime.with(LocalTime.of(hours, minutes));

            setDateTime(dateTime);
        }
    }

    public void onDateChosen(int year, int month, int dayOfMonth) {
        LocalDateTime dateTime = getDateTimeData().getValue();
        if (dateTime != null) {
            dateTime = dateTime.with(LocalDate.of(year, month, dayOfMonth));

            setDateTime(dateTime);
        }
    }


    private void updateReadyToLoad() {
        readyToLoad.setValue(src != null && target != null);
    }

    public LiveData<Location> getSrcData() {
        return srcData;
    }

    public LiveData<Location> getTargetData() {
        return targetData;
    }

    public LiveData<List<Journey>> getJourneysData() {
        return journeysData;
    }

    public LiveData<LocalDateTime> getDateTimeData() {
        return dateTimeData;
    }

    public LiveData<Boolean> getReadyToLoadData() {
        return readyToLoad;
    }

    public void setDateTime(LocalDateTime dateTime) {
        dateTimeData.setValue(dateTime);
    }
}
