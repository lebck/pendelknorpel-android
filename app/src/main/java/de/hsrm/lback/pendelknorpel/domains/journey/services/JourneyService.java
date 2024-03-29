package de.hsrm.lback.pendelknorpel.domains.journey.services;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.domains.journey.models.Journey;
import de.hsrm.lback.pendelknorpel.domains.journey.models.JourneyList;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.domains.journey.tasks.FetchJourneysTask;
import de.hsrm.lback.pendelknorpel.domains.journey.tasks.FetchMoreJourneysTask;
import de.hsrm.lback.pendelknorpel.domains.journey.tasks.RefreshJourneyTask;

public class JourneyService {
    public static void getAllJourneys(
            Location src,
            Location target,
            MutableLiveData<JourneyList> journeys,
            LocalDateTime dateTime
    ) {

        new FetchJourneysTask(journeys, dateTime).execute(src, target);

    }

    public static Journey getCurrentJourney(Context ctx) {
        // get current journey from sharedpreferences
        SharedPreferences preferences = ctx.getSharedPreferences(
                ctx.getString(R.string.current_journey),
                Context.MODE_PRIVATE
        );

        String json = preferences.getString(Journey.JOURNEY_ID, "");

        if (!json.equals("")) {
            try {
                return new ObjectMapper().readValue(json, Journey.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static void setCurrentJourney(Context ctx, Journey journey) {
        // get shared preferences for current journey
        SharedPreferences sharedPreferences =
                ctx.getSharedPreferences(ctx.getString(R.string.current_journey), Context.MODE_PRIVATE);

        // write serialized journey to preferences as current journey
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString(Journey.JOURNEY_ID, new ObjectMapper().writeValueAsString(journey));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        editor.apply();

    }

    public static void getMoreJourneys(Location src,
                                       Location target,
                                       JourneyList currentJourneys,
                                       MutableLiveData<JourneyList> journeysData,
                                       LocalDateTime dateTime) {
        new FetchMoreJourneysTask(currentJourneys, journeysData, dateTime, true).execute(src, target);
    }

    public static void getEarlierJourneys(Location src, Location target, JourneyList currentJourneys, MutableLiveData<JourneyList> journeysData, LocalDateTime dateTime) {
        new FetchMoreJourneysTask(currentJourneys, journeysData, dateTime, false).execute(src, target);
    }

    public static void refreshJourney(MutableLiveData<Journey> journeyData) {
        new RefreshJourneyTask(journeyData).execute();
    }
}
