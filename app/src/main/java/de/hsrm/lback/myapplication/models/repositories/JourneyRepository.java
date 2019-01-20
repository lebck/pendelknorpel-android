package de.hsrm.lback.myapplication.models.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.tasks.FetchJourneysTask;

public class JourneyRepository {
    public static void getAllJourneys (Location src, Location target, MutableLiveData<List<Journey>> journeys) {

        new FetchJourneysTask(journeys).execute(src, target);

    }

    public static Journey getCurrentJourney(Context ctx) {
        // get current journey from sharedpreferences
        SharedPreferences preferences = ctx.getSharedPreferences(
                ctx.getString(R.string.current_journey),
                Context.MODE_PRIVATE
        );

        String json = preferences.getString(Journey.JOURNEY_ID, "");

        return new Gson().fromJson(json, Journey.class);

    }
}
