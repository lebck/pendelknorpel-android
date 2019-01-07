package de.hsrm.lback.myapplication.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.views.views.LocationView;

public class LocationViewModel extends ViewModel implements View.OnDragListener{

    private Location location;

    private LocationView view;

    private String id;

    public LocationViewModel() {
        super();
    }

    public void init(Location location, LocationView view) {
        this.id = id;
        this.location = location;
        this.view = view;

        view.setOnTouchListener((v, e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ClipData.Item item = new ClipData.Item("");

                    ClipData dragData = new ClipData(
                            v.getTag().toString(),
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                            item
                    );

                    v.startDragAndDrop(dragData, new de.hsrm.lback.myapplication.views.views.DragShadowBuilder(v), v, 0);

                    return true;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    return true;
                default:
                    return false;
            }
        });

        view.setOnDragListener(this);

        setName(location.getName());

    }

    public void setName(String name) {
        location.setName(name);
        view.setText(name);
    }

    public String getName() {
        return location.getName();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {


        if (event.getAction() == DragEvent.ACTION_DROP) {

            LocationView target = (LocationView) v;
            LocationView src = (LocationView) event.getLocalState();

            Location srcLocation = src.getModel().location;
            Location targetLocation = target.getModel().location;

            ((LocationView)v).showDropSnackbar(srcLocation, targetLocation);

        }

        return true;
    }


}
