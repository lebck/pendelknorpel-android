package de.hsrm.lback.myapplication.domains.location.views;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.View;

import de.hsrm.lback.myapplication.domains.location.models.Location;

/**
 * VM of a location
 */
public class LocationViewModel extends ViewModel {
    private Location location;
    private boolean anonymous;
    private boolean gps;
    private ViewHandler viewHandler;
    @Nullable
    private LocationUpdateCallback locationUpdateCallback;
    public LocationViewModel() {
        this.anonymous = false;
        this.gps = false;
    }

    public void init(Location location, ViewHandler viewHandler) {
        this.location = location;
        this.viewHandler = viewHandler;
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
            boolean targetIsAnonymous = targetViewModel.isAnonymous();

            boolean srcIsGps = srcViewModel.isGps();
            boolean targetIsGps = targetViewModel.isGps();

            boolean srcAndTargetAreSet = srcLocation != null && targetLocation != null;

            if (target == src && !srcIsAnonymous && !srcIsGps) {   // if view is dropped on itself
                Location location = target.getViewModel().getLocation();
                int uid = 0;

                if (location != null) {
                    uid = location.getUid();
                }

                viewHandler.openEditView(uid);

            } else if (targetIsAnonymous && srcIsAnonymous) {

                viewHandler.openAnonymousEditView(0, 0);

            } else if (srcIsGps && !targetIsGps) {

                viewHandler.openGpsEditView(null, targetLocation);

            } else if (targetIsGps && !srcIsGps) {

                viewHandler.openGpsEditView(srcLocation, null);

            } else if (targetIsAnonymous) {

                viewHandler.openAnonymousEditView(srcLocation.getUid(), 0);

            } else if (srcIsAnonymous) {

                viewHandler.openAnonymousEditView(0, targetLocation.getUid());

            } else if (srcAndTargetAreSet) {

                // if view is dropped on other LocationView
                viewHandler.openJourneyOverview(srcLocation, targetLocation);
            }
        }

        return true;  // consume event
    }

    public interface LocationUpdateCallback {
        void onLocationUpdate(@Nullable Location newLocation);
    }

    public interface ViewHandler {
        void openEditView(int id);

        void openAnonymousEditView(int srcId, int targetId);

        void openJourneyOverview(Location srcLocation, Location targetLocation);

        void openGpsEditView(@Nullable Location srcLocation, @Nullable Location targetLocation);
    }
}
