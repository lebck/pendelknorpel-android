package de.hsrm.lback.pendelknorpel.domains.gpslist;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.util.Callback;
import de.hsrm.lback.pendelknorpel.util.adapters.LocationSearchAdapter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GpsListFragment extends Fragment {
    public static final int REQUEST_LOCATION_PERMISSION = 99;
    public static final int GPS_RESULT = 2;
    private GpsListViewModel viewModel;

    private ListView locationListView;
    private LinearLayout progressBar;

    private LocationSearchAdapter adapter;
    private LocationManager manager;

    @Nullable
    Callback<Location> onFinishedCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gps_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(GpsListViewModel.class);
        locationListView = view.findViewById(R.id.search_results);
        progressBar = view.findViewById(R.id.progress_bar);

        adapter = new LocationSearchAdapter(this.getLayoutInflater(), this::onLocationClicked);
        locationListView.setAdapter(adapter);

        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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

        if (onFinishedCallback != null) onFinishedCallback.handle(result);
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
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            viewModel.receiveLocation(this::onLocationReceived);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.grant_location_access),
                    REQUEST_LOCATION_PERMISSION,
                    perms
            );
        }
    }

    private void onLocationReceived(@Nullable android.location.Location location) {
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
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.turn_on_gps)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    private void onLocationsListReceived(List<Location> locations) {
        adapter.setLocations(locations);
        hideProgressBar();
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void setOnFinishedCallback(Callback<Location> onFinishedCallback) {
        this.onFinishedCallback = onFinishedCallback;
    }
}