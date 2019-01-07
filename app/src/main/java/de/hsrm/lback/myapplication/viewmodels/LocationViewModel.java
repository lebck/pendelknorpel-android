package de.hsrm.lback.myapplication.viewmodels;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.DialogInterface;
import android.view.DragEvent;
import android.view.View;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.persistence.LocationDao;
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

    public LiveData<String> getName() {
        return location.getName();
    }

    public void setName(String name) {
        this.location.setName(name);
        locationRepository.update(location);
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
                ((LocationView)v).showChangeName();
            } else { // if view is dropped on other LocationView
                Location srcLocation = src.getModel().location;
                Location targetLocation = target.getModel().location;

                ((LocationView)v).showDropSnackBar(srcLocation, targetLocation);

            }
        }

        return true;  // consume event
    }
}
