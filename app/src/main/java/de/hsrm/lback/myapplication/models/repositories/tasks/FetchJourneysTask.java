package de.hsrm.lback.myapplication.models.repositories.tasks;

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

        // TODO remove hardcoded location id
        src.setApiId(8000250);      // wiesbaden hbf
        target.setApiId(8000774);   // baden baden

        ApiConnector connector = new ApiConnector();

        List <Journey> journeys = null;

        try {
            // TODO remove hardcoded datetime
            journeys = connector.getDepartures(src, target, LocalDateTime.now());

            journeysData.postValue(journeys);


        } catch (IOException e) {
            e.printStackTrace();
            cancel(true);
        }

        return journeys;
    }
}
