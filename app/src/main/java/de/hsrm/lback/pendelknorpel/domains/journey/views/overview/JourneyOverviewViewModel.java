package de.hsrm.lback.pendelknorpel.domains.journey.views.overview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import de.hsrm.lback.pendelknorpel.domains.journey.models.JourneyList;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.services.JourneyService;
import de.hsrm.lback.pendelknorpel.services.LocationService;

/**
 * controls a list of journeysData
 */
public class JourneyOverviewViewModel extends AndroidViewModel {
    private LiveData<JourneyList> journeysData;
    private MutableLiveData<Location> srcData;
    private MutableLiveData<Location> targetData;
    private LocationService locationService;
    private MutableLiveData<LocalDateTime> dateTimeData;
    private MutableLiveData<Boolean> readyToLoad;

    public JourneyOverviewViewModel(Application application) {
        super(application);
        locationService = new LocationService(getApplication());
        readyToLoad = new MutableLiveData<>();
        readyToLoad.setValue(false);
        journeysData = new MutableLiveData<>();
        dateTimeData = new MutableLiveData<>();
        dateTimeData.setValue(LocalDateTime.now());
        srcData = new MutableLiveData<>();
        targetData = new MutableLiveData<>();
    }

    public void setSrc(int uid) {
        locationService.get(uid, srcData);
    }

    public void setSrc(String json) {
        srcData.postValue(LocationService.getLocationByJson(json));
    }

    public void setTarget(int uid) {
        locationService.get(uid, targetData);
    }

    public void setTarget(String json) {
        targetData.postValue(LocationService.getLocationByJson(json));
    }

    /** set target */
    public void onTargetChange(Location location) {
        updateReadyToLoad();
    }

    /** set src */
    public void onSrcChange(Location location) {
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
                getSrc(),
                getTarget(),
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
                getSrc(),
                getTarget(),
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
                getSrc(),
                getTarget(),
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
        readyToLoad.setValue(getSrc() != null && getTarget() != null);
    }

    private Location getSrc() {
        return this.srcData.getValue();
    }

    private Location getTarget() {
        return this.targetData.getValue();
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
