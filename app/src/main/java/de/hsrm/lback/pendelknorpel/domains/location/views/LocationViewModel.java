package de.hsrm.lback.pendelknorpel.domains.location.views;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.View;

import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.domains.location.views.overview.LocationOverviewStateMachine;

/**
 * VM of a location
 */
public class LocationViewModel extends ViewModel {
    private Location location;
    private boolean anonymous;
    private boolean gps;
    private LocationOverviewStateMachine stateMachine;
    @Nullable
    private LocationUpdateCallback locationUpdateCallback;

    public LocationViewModel(LocationOverviewStateMachine stateMachine) {
        this.stateMachine = stateMachine;
        this.anonymous = false;
        this.gps = false;
    }

    public void init(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location value) {
        this.location = value;
        if (locationUpdateCallback != null) {
            locationUpdateCallback.onLocationUpdate(this.location);
        }
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }

    public void setLocationUpdateCallback(LocationUpdateCallback locationUpdateCallback) {
        this.locationUpdateCallback = locationUpdateCallback;
        this.locationUpdateCallback.onLocationUpdate(this.getLocation());
    }

    public void onSrcAndTargetChosen(LocationViewModel srcViewModel, LocationViewModel targetViewModel) {
            boolean srcIsAnonymous = srcViewModel.isAnonymous();
            boolean same = srcViewModel.equals(targetViewModel);
            Location targetLocation = targetViewModel.getLocation();
            Location srcLocation = srcViewModel.getLocation();

            stateMachine.reset();

            if (same && !srcIsAnonymous && !srcViewModel.isGps()) {   // if view is dropped on itself
                int uid = 0;

                if (srcLocation != null) {
                    uid = srcLocation.getUid();
                }

                stateMachine.setEditState(uid);
                return;

            }

            if (srcViewModel.isEmpty() || targetViewModel.isEmpty()) { // one of the view is empty
                return;
            }


            stateMachine.setSrcGps(srcViewModel.isGps());
            stateMachine.setTargetGps(targetViewModel.isGps());

            if (targetLocation != null && srcLocation != null) {
                stateMachine.setBoth(srcLocation, targetLocation);
            } else if (srcLocation == null && targetLocation == null) {
                stateMachine.setBothState();
            } else if (targetLocation != null) {
                stateMachine.setTarget(targetLocation);
            } else if (srcLocation != null) { // srcLocation != null
                stateMachine.setSrc(srcLocation);
            }
    }

    public boolean isEmpty() {
        return !(this.isGps() || this.isAnonymous() || this.getLocation() != null);
    }

    public interface LocationUpdateCallback {
        void onLocationUpdate(@Nullable Location newLocation);
    }
}
