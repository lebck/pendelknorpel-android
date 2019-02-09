package de.hsrm.lback.myapplication.views.activities;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.helpers.adapters.LocationAdapter;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;
import de.hsrm.lback.myapplication.views.views.LocationView;

/**
 * Main actvity of the app.
 *
 * Displays a list of Locations and provides inputs to add
 * new Locations
 */
public class LocationOverview extends AppCompatActivity {
    private GridView locationsGrid;
    private LocationView anonymousLocationView;

    private LocationAdapter gridArrayAdapter;
    private List<Location> locations;
    private LocationRepository locationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_overview);

        // get views
        this.locationsGrid = findViewById(R.id.locations_grid);
        this.anonymousLocationView = findViewById(R.id.anonymous);

        this.locationRepository = new LocationRepository(this);

        // fetch location data and prepare list
        LiveData<List<Location>> locationData = locationRepository.getAllLocations();
        this.locations = new ArrayList<>();

        this.gridArrayAdapter = new LocationAdapter(this, locations, locationRepository, getApplication(), R.layout.location_layout);
        this.locationsGrid.setAdapter(gridArrayAdapter);

        // set Locations on change
        locationData.observe(this, this::onLocationsChange);

        // setup anonymous location view
        anonymousLocationView.init(this, new LocationViewModel(getApplication()));
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


    /**
     * open view to edit the location
     */
    public void openEditView(int uid) {
        Intent intent = new Intent(this, EditLocationView.class);

        intent.putExtra(Location.LOCATION_UID, uid);

        startActivity(intent);
    }

    private void openEditViewAnonymous() {
        Intent intent = new Intent(this, EditLocationView.class);

        startActivityForResult(intent, 0);
    }

    public void openJourneyOverview(Location srcLocation, Location targetLocation) {
        if (srcLocation != null && targetLocation != null) {
            // TODO open EditLocationView for locations that have id 0
            if (srcLocation.getUid() != 0 && targetLocation.getUid() != 0) { // both locations are already set
                openRegularJourneyOverview(srcLocation, targetLocation);
            }
            Log.d("journey", String.format("%s %s", srcLocation.toString(), targetLocation.toString()));
        } else if (srcLocation != null) {
            openEditViewAnonymous();
        }
    }


    private void openRegularJourneyOverview(Location srcLocation, Location targetLocation) {
        Intent intent = new Intent(this, JourneyOverview.class);
        intent.putExtra(Location.SRC_LOCATION, srcLocation.getUid());
        intent.putExtra(Location.DESTINATION_LOCATION, targetLocation.getUid());
        startActivity(intent);
    }
}
