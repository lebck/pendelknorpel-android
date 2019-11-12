package de.hsrm.lback.myapplication.views.journeyoverview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import de.hsrm.lback.myapplication.models.JourneyList;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.services.JourneyService;
import de.hsrm.lback.myapplication.services.LocationService;

/**
 * controls a list of journeysData
 */
public class JourneyOverviewViewModel extends AndroidViewModel {
    private Location src;
    private Location target;
    private LiveData<JourneyList> journeysData;
    private LiveData<Location> srcData;
    private LiveData<Location> targetData;
    private LocationService locationService;
    private MutableLiveData<LocalDateTime> dateTimeData;
    private MutableLiveData<Boolean> readyToLoad;

    public JourneyOverviewViewModel(Application application) {
        super(application);
    }

    public void init() {
        locationService = new LocationService(getApplication());
        readyToLoad = new MutableLiveData<>();
        readyToLoad.setValue(false);
        journeysData = new MutableLiveData<>();
        dateTimeData = new MutableLiveData<>();
        dateTimeData.setValue(LocalDateTime.now());
    }

    public void setSrc(int uid) {
        srcData = locationService.get(uid);
    }

    public void setSrc(String json) {
        srcData = new MutableLiveData<>();
        ((MutableLiveData<Location>)srcData).setValue(LocationService.getLocationByJson(json));
    }

    public void setTarget(int uid) {
        targetData = locationService.get(uid);
    }

    public void setTarget(String json) {
        targetData = new MutableLiveData<>();
        ((MutableLiveData<Location>)targetData).setValue(LocationService.getLocationByJson(json));
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

        JourneyService.getAllJourneys(
                src,
                target,
                (MutableLiveData<JourneyList>) journeysData,
                dateTime
        );
    }

    public void fetchMoreJourneys() {
        LocalDateTime dateTime = dateTimeData.getValue();

        // guard to prevent NullPointerException
        if (dateTime == null) dateTime = LocalDateTime.now();

        JourneyList currentJourneys = journeysData.getValue();

        if (currentJourneys == null) return;

        JourneyService.getMoreJourneys(
                src,
                target,
                currentJourneys,
                (MutableLiveData<JourneyList>) journeysData,
                dateTime
        );
    }

    public void fetchEarlierJourneys() {
        LocalDateTime dateTime = dateTimeData.getValue();

        // guard to prevent NullPointerException
        if (dateTime == null) dateTime = LocalDateTime.now();

        JourneyList currentJourneys = journeysData.getValue();

        if (currentJourneys == null) return;

        JourneyService.getEarlierJourneys(
                src,
                target,
                currentJourneys,
                (MutableLiveData<JourneyList>) journeysData,
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

    public LiveData<JourneyList> getJourneysData() {
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
