package de.hsrm.lback.pendelknorpel.helpers.tasks;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import de.hsrm.lback.pendelknorpel.domains.journey.models.Journey;
import de.hsrm.lback.pendelknorpel.network.ApiConnector;

public class RefreshJourneyTask extends AsyncTask<Void, Void, Void> {

    private MutableLiveData<Journey> journeyData;

    public RefreshJourneyTask(MutableLiveData<Journey> journeyData) {
        this.journeyData = journeyData;
    }

    @Override
    protected Void doInBackground(Void ...voids) {
        Journey refreshedJourney = new ApiConnector().refreshJourney(journeyData.getValue());
        journeyData.postValue(refreshedJourney);

        return null;
    }
}
