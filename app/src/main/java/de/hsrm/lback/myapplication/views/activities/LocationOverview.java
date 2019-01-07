package de.hsrm.lback.myapplication.views.activities;

import android.arch.lifecycle.LiveData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
    private Button addLocationButton;
    private EditText locationName;
    private List<Location> locations;

    private LocationRepository locationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_overview);

        this.locationRepository = new LocationRepository(this);

        LiveData<List<Location>> locationData = locationRepository.getAllLocations();
        this.locations = new ArrayList<>();


        this.locationsGrid = findViewById(R.id.locations_grid);
        this.addLocationButton = findViewById(R.id.add_location);
        this.locationName = findViewById(R.id.location_text);

        this.gridArrayAdapter = new LocationAdapter(this, locations);
        this.locationsGrid.setAdapter(gridArrayAdapter);

        locationData.observe(this, locationsList -> {
            Log.d("", locationsList.toString());
            this.locations.clear();
            this.locations.addAll(locationsList);
            this.gridArrayAdapter.notifyDataSetChanged();
        });

        this.addLocationButton.setOnClickListener(e ->
            this.locationRepository.insert(
                    new Location(locationName.getText().toString(), 0)
            )
        );

    }
}
