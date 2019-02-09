package de.hsrm.lback.myapplication.network;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import de.hsrm.lback.myapplication.network.ApiConnector;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;

public class FetchJourneysTask extends AsyncTask<Location, Void, List<Journey>> {
    private MutableLiveData<List<Journey>> journeysData;

    public FetchJourneysTask(MutableLiveData<List<Journey>> journeysData) {
        this.journeysData = journeysData;
    }

    @Override
    protected List<Journey> doInBackground(Location... locations) {
        Location src = locations[0];
        Location target = locations[1];

        ApiConnector connector = new ApiConnector();

        List <Journey> journeys = null;

        // TODO remove hardcoded datetime
        journeys = connector.getDepartures(src, target, LocalDateTime.now());

        journeysData.postValue(journeys);


        return journeys;
    }
}
