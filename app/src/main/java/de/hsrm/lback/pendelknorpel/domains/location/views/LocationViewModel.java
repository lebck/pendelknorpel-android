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
    @Nullable private LocationUpdateCallback locationUpdateCallback;

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

    public boolean onDrag(View v, DragEvent event) {

        if (event.getAction() == DragEvent.ACTION_DROP) {  // when view is dropped

            LocationView src = (LocationView) event.getLocalState();
            LocationView target = (LocationView) v;

            LocationViewModel srcViewModel = src.getViewModel();
            LocationViewModel targetViewModel = target.getViewModel();

            Location srcLocation = srcViewModel.getLocation();
            Location targetLocation = targetViewModel.getLocation();

            boolean srcIsAnonymous = srcViewModel.isAnonymous();

            if (target == src && !srcIsAnonymous && !srcViewModel.isGps()) {   // if view is dropped on itself
                Location location = target.getViewModel().getLocation();
                int uid = 0;

                if (location != null) {
                    uid = location.getUid();
                }

                stateMachine.setEditState(uid);
                return true;

            }

            stateMachine.setSrcGps(srcViewModel.isGps());
            stateMachine.setTargetGps(targetViewModel.isGps());

            if (srcLocation == null && targetLocation == null) {
                stateMachine.setBothState();
            } else if (targetLocation != null) {
                stateMachine.setTarget(targetLocation);
            } else { // srcLocation != null
                stateMachine.setSrc(srcLocation);
            }
        }

        return true;  // consume event
    }

    public interface LocationUpdateCallback {
        void onLocationUpdate(@Nullable Location newLocation);
    }
}
