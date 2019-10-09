package de.hsrm.lback.myapplication.network;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import de.hsrm.lback.myapplication.models.JourneyList;
import de.hsrm.lback.myapplication.network.ApiConnector;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;

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

        ApiConnector connector = new ApiConnector();

        JourneyList journeys = null;

        journeys = connector.getDepartures(src, target, dateTime);

        journeysData.postValue(journeys);


        return journeys;
    }
}
