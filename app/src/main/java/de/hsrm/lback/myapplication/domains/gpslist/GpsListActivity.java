package de.hsrm.lback.myapplication.domains.gpslist;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.adapters.LocationSearchAdapter;
import de.hsrm.lback.myapplication.domains.location.models.Location;
import de.hsrm.lback.myapplication.services.LocationService;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GpsListActivity extends AppCompatActivity {
    public static final int REQUEST_LOCATION_PERMISSION = 99;
    public static final int GPS_RESULT = 2;
    private GpsListViewModel viewModel;

    private ListView locationListView;
    private LinearLayout progressBar;

    private LocationSearchAdapter adapter;
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_list);
        viewModel = ViewModelProviders.of(this).get(GpsListViewModel.class);
        locationListView = findViewById(R.id.search_results);
        progressBar = findViewById(R.id.progress_bar);

        adapter = new LocationSearchAdapter(this.getLayoutInflater(), this::onLocationClicked);
        locationListView.setAdapter(adapter);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        requestLocationPermission();

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // if gps turned off
            showGpsOffAlert();
        }
    }

    private void onLocationClicked(View view) {
        String name = ((TextView) view).getText().toString();
        String apiId = (String) view.getTag();

        Location result = new Location(name);
        result.setApiId(apiId);

        finish(result);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            viewModel.receiveLocation(this::onLocationReceived);
        }
        else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.grant_location_access),
                    REQUEST_LOCATION_PERMISSION,
                    perms
            );
        }
    }

    private void onLocationReceived(@Nullable  android.location.Location location) {
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            LiveData<List<Location>> locationData = viewModel.getLocationsForGpsCoord(lat, lon);

            locationData.observe(this, this::onLocationsListReceived);
        } else {
            showGpsOffAlert();
        }
    }

    private void showGpsOffAlert() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.turn_on_gps)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    private void onLocationsListReceived(List<Location> locations) {
        adapter.setLocations(locations);
        hideProgressBar();
    }

    private void finish(Location result) {
        Intent intent = new Intent();
        intent.putExtra(Location.SERIALIZED_LOCATION, LocationService.serializeLocation(result));

        setResult(GPS_RESULT, intent);

        finish();
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}