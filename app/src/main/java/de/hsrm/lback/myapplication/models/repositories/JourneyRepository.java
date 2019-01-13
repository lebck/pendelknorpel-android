package de.hsrm.lback.myapplication.models.repositories;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.tasks.FetchJourneysTask;

public class JourneyRepository {
    public static void getAllJourneys (Location src, Location target, MutableLiveData<List<Journey>> journeys) {

        new FetchJourneysTask(journeys).execute(src, target);

    }
}
