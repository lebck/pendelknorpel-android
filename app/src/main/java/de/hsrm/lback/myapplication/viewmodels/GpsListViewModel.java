package de.hsrm.lback.myapplication.viewmodels;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.services.LocationService;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class GpsListViewModel extends AndroidViewModel {
    private LocationService locationService;
    private FusedLocationProviderClient client;

    public GpsListViewModel(@NonNull Application application) {
        super(application);
        this.locationService = new LocationService(application);
        client = LocationServices.getFusedLocationProviderClient(application);
    }

    public void receiveLocation(@NonNull OnSuccessListener<android.location.Location> onSuccessListener) {
        try {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(100);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationCallback callback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    onSuccessListener.onSuccess(locationResult.getLastLocation());
                    client.removeLocationUpdates(this);
                }
            };

            client.requestLocationUpdates(locationRequest, callback, null);
        } catch (SecurityException ignored) {
            System.err.println("AAA");
        }

    }
    public LiveData<List<Location>>
        getLocationsForGpsCoord(double lat, double lon) {

        return locationService.getLocationsForGpsCoords(lat, lon);
    }

}
