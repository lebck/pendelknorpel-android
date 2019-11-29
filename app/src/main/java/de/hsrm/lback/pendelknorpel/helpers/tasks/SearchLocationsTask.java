package de.hsrm.lback.pendelknorpel.helpers.tasks;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;

import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.network.ApiConnector;

/**
 * Task that fetches Locations matching given search term
 */
public class SearchLocationsTask extends AsyncTask<String, Void, List<Location>> {
    private MutableLiveData<List<Location>> resultsData;

    public SearchLocationsTask(MutableLiveData<List<Location>> resultsData) {
        this.resultsData = resultsData;
    }

    @Override
    protected List<Location> doInBackground(String... searchTerms) {
        String searchTerm = searchTerms[0];

        ApiConnector connector = ApiConnector.getInstance();

        List<Location> locations = connector.getLocationsBySearchTerm(searchTerm);


        resultsData.postValue(locations);

        return locations;
    }
}
