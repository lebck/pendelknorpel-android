package de.hsrm.lback.myapplication.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.view.DragEvent;
import android.view.View;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.views.views.LocationView;

/**
 * VM of a location
 */
public class LocationViewModel extends ViewModel implements View.OnDragListener{

    private Location location;
    private LocationRepository locationRepository;

    public LocationViewModel(LocationRepository locationRepository) {
        super();
        this.locationRepository = locationRepository;
    }

    public void init(Location location) {
        this.location = location;

    }

    /**
     * executes when a LocationView is dropped on another (or the same) LocationView
     */
    @Override
    public boolean onDrag(View v, DragEvent event) {


        if (event.getAction() == DragEvent.ACTION_DROP) {  // when view is dropped

            LocationView target = (LocationView) v;
            LocationView src = (LocationView) event.getLocalState();

            if (target == src) {   // if view is dropped on itself

                ((LocationView)v).openEditView();


            } else { // if view is dropped on other LocationView
                Location srcLocation = src.getViewModel().location;
                Location targetLocation = target.getViewModel().location;

                ((LocationView)v).showDropSnackBar(srcLocation, targetLocation);

            }
        }

        return true;  // consume event
    }

    public void update() {
        locationRepository.update(this.location);
    }

    public Location getLocation() {
        return this.location;
    }

}
