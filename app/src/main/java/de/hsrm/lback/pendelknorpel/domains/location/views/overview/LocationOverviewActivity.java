package de.hsrm.lback.pendelknorpel.domains.location.views.overview;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;

import java.util.Collections;
import java.util.List;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.domains.gpslist.GpsListFragment;
import de.hsrm.lback.pendelknorpel.domains.journey.views.overview.JourneyOverviewActivity;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.domains.location.views.LocationView;
import de.hsrm.lback.pendelknorpel.domains.location.views.LocationViewModel;
import de.hsrm.lback.pendelknorpel.domains.location.views.edit.EditLocationActivity;
import de.hsrm.lback.pendelknorpel.domains.location.views.edit.EditLocationViewModel;
import de.hsrm.lback.pendelknorpel.domains.location.views.edit.components.search.SearchLocationFragment;
import de.hsrm.lback.pendelknorpel.domains.location.views.edit.components.search.SearchLocationViewModel;
import de.hsrm.lback.pendelknorpel.helpers.Callback;
import de.hsrm.lback.pendelknorpel.helpers.adapters.LocationAdapter;
import de.hsrm.lback.pendelknorpel.services.LocationService;
import de.hsrm.lback.pendelknorpel.services.WindowService;

import static de.hsrm.lback.pendelknorpel.domains.location.views.edit.EditLocationViewModel.ANONYMOUS_UID;

/**
 * Main activity of the app.
 * <p>
 * Displays a list of Locations and provides inputs to add
 * new Locations
 */
public class LocationOverviewActivity extends AppCompatActivity implements LocationOverviewStateMachine.OnChangeCallback {
    private static final String AN_SRC = "an_src";
    private static final String AN_TARGET = "an_target";
    private static final int GPS_SRC = 2;
    private static final int GPS_TARGET = 3;

    private int locationBubbleAmount;

    private GridView locationsGrid;
    private LocationView anonymousLocationView;
    private LocationView gpsLocationView;

    private LocationAdapter gridArrayAdapter;

    private WindowService windowService;

    private LocationOverviewViewModel viewModel;
    private SearchLocationViewModel searchViewModel;

    private LocationOverviewStateMachine stateMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_overview);

        // get views
        this.locationsGrid = findViewById(R.id.locations_grid);
        this.anonymousLocationView = findViewById(R.id.anonymous);
        this.gpsLocationView = findViewById(R.id.gps);

        this.windowService = new WindowService(this);

        this.initializeRows();

        this.viewModel = ViewModelProviders.of(this).get(LocationOverviewViewModel.class);
        this.searchViewModel = new EditLocationViewModel(getApplication(), ANONYMOUS_UID);

        stateMachine = new LocationOverviewStateMachine();
        stateMachine.setStateChangeCallback(this);

        this.gridArrayAdapter = new LocationAdapter(Collections.emptyList(), stateMachine, getLayoutInflater());
        this.locationsGrid.setAdapter(gridArrayAdapter);
        this.locationsGrid.setColumnWidth(this.windowService.calculateMeasures(300));

        // set Locations on change
        this.viewModel.getLocationsData().observe(this, this::onLocationsChange);
        this.viewModel.getAnonymousLocationsData().observe(this, this::onAnonymousLocationsChange);

        initializeStaticLocations();
    }

    public void initializeRows() {
        int rowAmount = (this.windowService.getHeight() - (int) windowService.pxFromDp(120)) /
                this.windowService.calculateMeasures(300)
                - 2;

        this.locationBubbleAmount = rowAmount * 3;
    }

    /**
     * setup anonymous location view and gps location view
     */
    private void initializeStaticLocations() {
        LocationViewModel anonymousLocationViewModel = new LocationViewModel(stateMachine);
        anonymousLocationViewModel.setAnonymous(true);
        anonymousLocationViewModel.init(null);
        anonymousLocationView.init(anonymousLocationViewModel);

        LayoutParams params = anonymousLocationView.getLayoutParams();
        params.width = this.windowService.calculateMeasures(300);
        anonymousLocationView.setLayoutParams(params);

        LocationViewModel gpsLocationViewModel = new LocationViewModel(stateMachine);
        gpsLocationViewModel.setGps(true);
        gpsLocationViewModel.init(null);
        gpsLocationView.init(gpsLocationViewModel);

        gpsLocationView.setLayoutParams(params);
    }

    /**
     * Update all locations in grid
     */
    private void onLocationsChange(List<Location> locations) {
        // fill up locations with empty locations until there are enough
        int len = locations.size();
        for (int i = 0; i < this.locationBubbleAmount - len; i++) {
            locations.add(null);
        }
        this.gridArrayAdapter.setLocations(locations);
        this.gridArrayAdapter.notifyDataSetChanged();

    }

    /**
     * open view to edit the location
     */
    public void openEditView(int uid) {
        Intent intent = new Intent(this, EditLocationActivity.class);

        intent.putExtra(Location.LOCATION_UID, uid);

        startActivity(intent);
    }

    public void openGpsEditView(boolean isSrc) {
        GpsListFragment fragment = new GpsListFragment();

        Callback<Location> callback = location -> onLocationChosen(location, isSrc, fragment);

        fragment.setOnFinishedCallback(callback);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .add(R.id.location_overview_root, fragment)
                .commit();
    }

    private void openAnonymousEditView(boolean isSourceLocation) {
        SearchLocationFragment fragment = new SearchLocationFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .add(R.id.fragment_journey_detail, fragment)
                .commit();

        fragment.setOnClickCallback(location -> this.onLocationChosen(location, isSourceLocation, fragment));
        fragment.setViewModel(searchViewModel);
    }

    private void onLocationChosen(Location location, boolean isSourceLocation, Fragment removeFragment) {
        searchViewModel.cleanUp();
        if (isSourceLocation) {
            stateMachine.setSrc(location);
        } else {
            stateMachine.setTarget(location);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .remove(removeFragment)
                .commit();

    }

    private void onAnonymousLocationsChange(
            @Nullable LocationOverviewViewModel.AnonymousLocations locations
    ) {
        if (locations != null) {
            Location src = locations.anonymousSrcLocation;
            Location target = locations.anonymousTargetLocation;

            if (!locations.isSrcGps && src == null) {
                openAnonymousEditView(true);
            } else if (!locations.isTargetGps && target == null) {
                openAnonymousEditView(false);
            } else if (src != null && target != null) {
                viewModel.cleanUp();
                searchViewModel.cleanUp();
                openJourneyOverview(src, target);
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
    }

    private void openJsonBasedJourneyOverview(Location src, Location target) {
        String srcJson = LocationService.serializeLocation(src);
        String targetJson = LocationService.serializeLocation(target);

        Intent intent = new Intent(this, JourneyOverviewActivity.class);

        intent.putExtra(JourneyOverviewActivity.SRC_JSON, srcJson);
        intent.putExtra(JourneyOverviewActivity.TARGET_JSON, targetJson);

        startActivity(intent);
    }

    /**
     * open journey overview of given Locations.
     */
    private void openRegularJourneyOverview(Location srcLocation, Location targetLocation) {
        Intent intent = new Intent(this, JourneyOverviewActivity.class);
        intent.putExtra(Location.SRC_LOCATION, srcLocation.getUid());
        intent.putExtra(Location.DESTINATION_LOCATION, targetLocation.getUid());
        startActivity(intent);
    }

    @Override
    public void handleSourceState(boolean gps) {
        if (gps) {
            openGpsEditView(true);
        } else {
            openAnonymousEditView(true);
        }
    }

    @Override
    public void handleTargetState(boolean gps) {
        if (gps) {
            openGpsEditView(false);
        } else {
            openAnonymousEditView(false);
        }
    }

    @Override
    public void handleDoneState(Location src, Location target) {
        openJourneyOverview(src, target);
    }

    @Override
    public void handleEditState(int uid) {
        openEditView(uid);
    }
}
