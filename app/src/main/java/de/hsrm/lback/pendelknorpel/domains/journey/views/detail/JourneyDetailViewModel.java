package de.hsrm.lback.pendelknorpel.domains.journey.views.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import de.hsrm.lback.pendelknorpel.domains.journey.models.Journey;
import de.hsrm.lback.pendelknorpel.domains.journey.services.JourneyService;

public class JourneyDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Journey> journeyData;

    public JourneyDetailViewModel(Application application) {
        super(application);

        this.journeyData = new MutableLiveData<>();
        this.journeyData.postValue(JourneyService.getCurrentJourney(this.getApplication().getApplicationContext()));
    }


    public void refreshJourney() {
        JourneyService.refreshJourney(journeyData);
    }

    public MutableLiveData<Journey> getJourneyData() {
        return journeyData;
    }
}
