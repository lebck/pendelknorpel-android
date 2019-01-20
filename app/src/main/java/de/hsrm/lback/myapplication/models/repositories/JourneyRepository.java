package de.hsrm.lback.myapplication.models.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
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

    public static void setCurrentJourney(Context ctx, Journey journey) {
        // get shared preferences for current journey
        SharedPreferences sharedPreferences =
                ctx.getSharedPreferences(ctx.getString(R.string.current_journey), Context.MODE_PRIVATE);

        // write serialized journey to preferences as current journey
        SharedPreferences.Editor editor = sharedPreferences.edit();

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(MutableLiveData.class, new JsonDeserializer<MutableLiveData<String>>() {
            @Override
            public MutableLiveData<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                MutableLiveData<String> data = new MutableLiveData<>();

                data.postValue(json.getAsString());

                return data;
            }
        });


        editor.putString(Journey.JOURNEY_ID, new Gson().toJson(journey));

        editor.apply();

    }
}
