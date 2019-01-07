package de.hsrm.lback.myapplication.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.helpers.adapters.LocationAdapter;

public class LocationOverview extends AppCompatActivity {
    private GridView locationsGrid;
    private LocationAdapter gridArrayAdapter;
    private Button addLocationButton;
    private EditText locationName;
    private List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_overview);
        this.locations = LocationRepository.getLocations();
        this.locationsGrid = findViewById(R.id.locations_grid);
        this.addLocationButton = findViewById(R.id.add_location);
        this.locationName = findViewById(R.id.location_text);

        this.gridArrayAdapter = new LocationAdapter(this, locations);
        this.locationsGrid.setAdapter(gridArrayAdapter);

        this.addLocationButton.setOnClickListener(e -> {
            this.locations.add(new Location(locationName.getText().toString(), 0));
            this.gridArrayAdapter.notifyDataSetChanged();
        });

    }




}
