package de.hsrm.lback.myapplication.views.activities;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
    private static final int LOCATION_BUBBLE_AMOUNT = 12;
    private static final String AN_SRC = "an_src";
    private static final String AN_TARGET = "an_target";
    private static final int GPS_SRC = 2;
    private static final int GPS_TARGET = 3;
    private GridView locationsGrid;
    private LocationView anonymousLocationView;
    private LocationView gpsLocationView;

    private LocationAdapter gridArrayAdapter;
    private List<Location> locations;
    private LocationRepository locationRepository;

    private Location anonymousSrcLocation;
    private Location anonymousTargetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreAnonymousLocations(savedInstanceState);
        setContentView(R.layout.activity_location_overview);

        // get views
        this.locationsGrid = findViewById(R.id.locations_grid);
        this.anonymousLocationView = findViewById(R.id.anonymous);
        this.gpsLocationView = findViewById(R.id.gps);

        this.locationRepository = new LocationRepository(this);

        // fetch location data and prepare list
        LiveData<List<Location>> locationData = locationRepository.getAllLocations();
        this.locations = new ArrayList<>();

        this.gridArrayAdapter = new LocationAdapter(this, locations, locationRepository, getApplication(), R.layout.location_layout);
        this.locationsGrid.setAdapter(gridArrayAdapter);

        // set Locations on change
        locationData.observe(this, this::onLocationsChange);

        initializeStaticLocations();
    }

     /** setup anonymous location view and gps location view */
     private void initializeStaticLocations() {
        LocationViewModel anonymousLocationViewModel = new LocationViewModel(getApplication());
        anonymousLocationViewModel.setAnonymous(true);
        anonymousLocationView.init(this, anonymousLocationViewModel);
        TextView anonymousLocationName = anonymousLocationView.findViewById(R.id.location_name);
        ImageView anonymousLocationLogo = anonymousLocationView.findViewById(R.id.location_logo);

        anonymousLocationName.setText(R.string.anonymous);

        LocationViewModel gpsLocationViewModel = new LocationViewModel(getApplication());
        gpsLocationViewModel.setGps(true);
        gpsLocationView.init(this, gpsLocationViewModel);
        TextView gpsLocationName = gpsLocationView.findViewById(R.id.location_name);
        ImageView gpsLocationLogo = gpsLocationView.findViewById(R.id.location_logo);

        gpsLocationName.setText(getString(R.string.gps));
        gpsLocationLogo.setImageResource(R.drawable.icon_gps);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /** Update all locations in grid */
    private void onLocationsChange(List<Location> locations) {


        // fill up locationslist with empty locations until there are enough
        int len = locations.size();
        for (int i = 0; i < LOCATION_BUBBLE_AMOUNT - len; i++) {
            locations.add(new Location(""));
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

    /** open view(s) to edit anonymous location(s) */
    public void openAnonymousEditView(int srcUid, int targetUid) {
        if (srcUid != 0) {
            locationRepository
                    .get(srcUid)
                    .observe(this, location -> anonymousSrcLocation = location);
        } else {
            openAnonymousEditView(EditLocationView.ANONYMOUS_SRC);
            return;
        }

        if (targetUid != 0) {
            locationRepository
                    .get(targetUid)
                    .observe(this, location -> anonymousTargetLocation = location);
        } else {
            openAnonymousEditView(EditLocationView.ANONYMOUS_TARGET);
        }

    }

    private void openAnonymousEditView(int requestCode) {
        Intent intent = new Intent(this, EditLocationView.class);
        intent.putExtra(Location.LOCATION_UID, EditLocationView.ANONYMOUS_UID);
        intent.putExtra("code", requestCode);

        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            String s = data.getStringExtra(Location.SERIALIZED_LOCATION);
            Location l = LocationRepository.getLocationByJson(s);
            switch (requestCode) {
                case EditLocationView.ANONYMOUS_SRC:
                case GPS_SRC:
                    anonymousSrcLocation = l;
                    break;
                case EditLocationView.ANONYMOUS_TARGET:
                case GPS_TARGET:
                    anonymousTargetLocation = l;
                    break;
            }

            if (anonymousSrcLocation == null && anonymousTargetLocation != null) {
                openAnonymousEditView(EditLocationView.ANONYMOUS_SRC);

            } else if (anonymousSrcLocation != null && anonymousTargetLocation == null) {
                openAnonymousEditView(EditLocationView.ANONYMOUS_TARGET);

            } else {
                openJourneyOverview(anonymousSrcLocation, anonymousTargetLocation);

                anonymousSrcLocation = null;
                anonymousTargetLocation = null;
            }
        }
    }

    /**
     * open journey overview of given Locations.
     * Open anonymous editView for locations that are null
     */
    public void openJourneyOverview(Location srcLocation, Location targetLocation) {
        if (srcLocation.getUid() != 0 && targetLocation.getUid() != 0) { // both locations are already set
            openRegularJourneyOverview(srcLocation, targetLocation);
        } else {
            openJsonBasedJourneyOverview(srcLocation, targetLocation);
        }
        Log.d("journey", String.format("%s %s", srcLocation.toString(), targetLocation.toString()));

    }

    private void openJsonBasedJourneyOverview(Location src, Location target) {
        String srcJson = LocationRepository.serializeLocation(src);
        String targetJson = LocationRepository.serializeLocation(target);

        Intent intent = new Intent(this, JourneyOverview.class);

        intent.putExtra(JourneyOverview.SRC_JSON, srcJson);
        intent.putExtra(JourneyOverview.TARGET_JSON, targetJson);

        startActivity(intent);
    }

    /**
     * open journey overview of given Locations.
     */
    private void openRegularJourneyOverview(Location srcLocation, Location targetLocation) {
        Intent intent = new Intent(this, JourneyOverview.class);
        intent.putExtra(Location.SRC_LOCATION, srcLocation.getUid());
        intent.putExtra(Location.DESTINATION_LOCATION, targetLocation.getUid());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (anonymousSrcLocation != null)
            outState.putString(AN_SRC, LocationRepository.serializeLocation(anonymousSrcLocation));
        if (anonymousTargetLocation != null)
            outState.putString(AN_TARGET, LocationRepository.serializeLocation(anonymousSrcLocation));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreAnonymousLocations(savedInstanceState);
    }

    private void restoreAnonymousLocations(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String src = savedInstanceState.getString(AN_SRC, null);
            String target = savedInstanceState.getString(AN_TARGET, null);

            if (src != null)
                anonymousSrcLocation = LocationRepository.getLocationByJson(src);
            if (target != null)
                anonymousTargetLocation = LocationRepository.getLocationByJson(target);
        }
    }

    public void openGpsEditView(@Nullable Location srcLocation, @Nullable Location targetLocation) {
        int requestCode;

        if (srcLocation != null) {
            requestCode = GPS_TARGET;
            anonymousSrcLocation = srcLocation;
        } else {
            requestCode = GPS_SRC;
            anonymousTargetLocation = targetLocation;
        }

        Intent intent = new Intent(this, GpsLIstActivity.class);

        startActivityForResult(intent, requestCode);
    }
}
