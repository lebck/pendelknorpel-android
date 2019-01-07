package de.hsrm.lback.myapplication.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.view.DragEvent;
import android.view.View;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.views.views.LocationView;

public class LocationViewModel extends ViewModel implements View.OnDragListener{

    private Location location;


    private String id;

    public LocationViewModel() {
        super();
    }

    public void init(Location location) {
        this.id = id;
        this.location = location;

    }

    public LiveData<String> getName() {
        return location.getName();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {


        if (event.getAction() == DragEvent.ACTION_DROP) {

            LocationView target = (LocationView) v;
            LocationView src = (LocationView) event.getLocalState();

            Location srcLocation = src.getModel().location;
            Location targetLocation = target.getModel().location;

            ((LocationView)v).showDropSnackBar(srcLocation, targetLocation);

        }

        return true;
    }


}
