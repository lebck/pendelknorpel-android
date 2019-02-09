package de.hsrm.lback.myapplication.views.activities;

import android.arch.lifecycle.LiveData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.helpers.adapters.LocationAdapter;

/**
 * Main actvity of the app.
 *
 * Displays a list of Locations and provides inputs to add
 * new Locations
 */
public class LocationOverview extends AppCompatActivity {
    private GridView locationsGrid;
    private LocationAdapter gridArrayAdapter;
    private List<Location> locations;

    private LocationRepository locationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_overview);

        // get views
        this.locationsGrid = findViewById(R.id.locations_grid);

        this.locationRepository = new LocationRepository(this);

        // fetch location data and prepare list
        LiveData<List<Location>> locationData = locationRepository.getAllLocations();
        this.locations = new ArrayList<>();

        this.gridArrayAdapter = new LocationAdapter(this, locations, locationRepository, getApplication(), R.layout.location_layout);
        this.locationsGrid.setAdapter(gridArrayAdapter);

        // set Locations on change
        locationData.observe(this, this::onLocationsChange);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void onLocationsChange(List<Location> locations) {


        // fill up locationslist with empty locations until there are 9
        int len = locations.size();
        for (int i = 0; i < 9 - len; i++) {
            locations.add(new Location("", 0));
        }

        // TODO make this less ugly:
        this.locations.clear();
        this.locations.addAll(locations);
        this.gridArrayAdapter.notifyDataSetChanged();

    }
}
