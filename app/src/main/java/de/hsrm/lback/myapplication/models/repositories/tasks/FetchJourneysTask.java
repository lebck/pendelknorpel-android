package de.hsrm.lback.myapplication.models.repositories.tasks;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.hsrm.lback.myapplication.models.Connection;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.Vehicle;

public class FetchJourneysTask extends AsyncTask<Location, Void, List<Journey>> {
    private MutableLiveData<List<Journey>> journeysData;

    public FetchJourneysTask(MutableLiveData<List<Journey>> journeysData) {
        this.journeysData = journeysData;
    }

    @Override
    protected List<Journey> doInBackground(Location... locations) {
        Location src = locations[0];
        Location target = locations[1];

        // TODO make network request to DB API
        int m = 100;
        List<Journey> journeys = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < m; i++) {

            List <Connection> connections = Collections.singletonList(
                    new Connection(
                            LocalDateTime.now().plusHours(i),
                            LocalDateTime.now().plusHours(1 + i),
                            Integer.toString(r.nextInt(10)),
                            Vehicle.BUS)
            );
            journeys.add(new Journey(src, target, connections));
        }

        try {
            Thread.sleep(2000); // fake network throttle
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.journeysData.postValue(journeys);

        return journeys;
    }
}
