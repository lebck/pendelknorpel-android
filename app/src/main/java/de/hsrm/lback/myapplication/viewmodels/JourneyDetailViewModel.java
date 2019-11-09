package de.hsrm.lback.myapplication.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.repositories.JourneyRepository;

public class JourneyDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Journey> journeyData;

    public JourneyDetailViewModel(Application application) {
        super(application);

        this.journeyData = new MutableLiveData<>();
        this.journeyData.postValue(JourneyRepository.getCurrentJourney(this.getApplication().getApplicationContext()));
    }


    public void refreshJourney() {
        JourneyRepository.refreshJourney(journeyData);
    }

    public MutableLiveData<Journey> getJourneyData() {
        return journeyData;
    }
}
