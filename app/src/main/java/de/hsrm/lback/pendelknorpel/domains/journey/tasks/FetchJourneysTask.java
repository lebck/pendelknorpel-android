package de.hsrm.lback.pendelknorpel.domains.journey.tasks;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.time.LocalDateTime;

import de.hsrm.lback.pendelknorpel.domains.journey.models.JourneyList;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.network.ApiConnector;

/**
 * Task that fetches journeys from given start and end location
 */
public class FetchJourneysTask extends AsyncTask<Location, Void, JourneyList> {
    private MutableLiveData<JourneyList> journeysData;
    private LocalDateTime dateTime;

    public FetchJourneysTask(MutableLiveData<JourneyList> journeysData, LocalDateTime dateTime) {
        this.journeysData = journeysData;
        this.dateTime = dateTime;
    }

    @Override
    protected JourneyList doInBackground(Location... locations) {
        Location src = locations[0];
        Location target = locations[1];

        ApiConnector connector = ApiConnector.getInstance();

        JourneyList journeys;

        journeys = connector.getDepartures(src, target, dateTime);

        journeysData.postValue(journeys);


        return journeys;
    }
}
