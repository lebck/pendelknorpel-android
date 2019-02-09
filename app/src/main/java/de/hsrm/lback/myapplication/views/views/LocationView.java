package de.hsrm.lback.myapplication.views.views;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.LocationDragShadowBuilder;
import de.hsrm.lback.myapplication.helpers.ResourcesHelper;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;
import de.hsrm.lback.myapplication.views.activities.EditLocationView;
import de.hsrm.lback.myapplication.views.activities.JourneyOverview;
import de.hsrm.lback.myapplication.views.activities.LocationOverview;


/**
 * Represents a single location
 *
 */
public class LocationView extends LinearLayout implements View.OnDragListener {

    private Paint textPaint;
    private LocationViewModel viewModel;
    private LocationOverview activity;
    private TextView locationNameView;
    private ImageView locationLogoView;

    public LocationView(Context context) {
        super(context);
    }

    public LocationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LocationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LocationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(LocationOverview activity, LocationViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
        this.locationNameView = findViewById(R.id.location_name);
        this.locationLogoView = findViewById(R.id.location_logo);

        this.setTag("connection_view");

        // start drag and drop instantly when view is touched
        this.setOnTouchListener((v, e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ClipData.Item item = new ClipData.Item("");

                    ClipData dragData = new ClipData(
                            v.getTag().toString(),
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                            item
                    );

                    v.startDragAndDrop(dragData, new LocationDragShadowBuilder(v), v, 0);

                    return true;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    return true;
                default:
                    return false;
            }
        });

        // set viewmodel to process the drop
        this.setOnDragListener(this);

        if (viewModel.getLocation() != null) {
            Location location = viewModel.getLocation();
            // set logo and name
            if (location.getDisplayName() == null || location.getDisplayName().equals(""))
                this.onNameChanged(location.getName());
            else
                this.onNameChanged(viewModel.getLocation().getDisplayName());

            this.onLogoChanged(viewModel.getLocation().getLogo());
        }


    }

    private void onLogoChanged(String s) {

        int resId = ResourcesHelper.getResId(s, R.drawable.class);
        if (resId > 0) this.locationLogoView.setImageResource(resId);

    }

    private void onNameChanged(String name) {
        this.locationNameView.setText(name);

        // make textview invisible if no name is set
        if (name.trim().equals(""))
            this.locationNameView.setVisibility(GONE);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * show snackbar with target and source location
     */
    public void showDropSnackBar(Location src, Location target) {
        Snackbar snackbar = Snackbar.make(
                this,
                String.format("Journey from %s to %s", src.getName(), target.getName()),
                Snackbar.LENGTH_SHORT
        );

        snackbar.show();
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

                activity.openEditView(target.getViewModel().getLocation().getUid());


            } else { // if view is dropped on other LocationView
                Location srcLocation = src.getViewModel().getLocation();
                Location targetLocation = target.getViewModel().getLocation();

                activity.openJourneyOverview(srcLocation, targetLocation);

            }
        }

        return true;  // consume event
    }



    public LocationViewModel getViewModel() {
        return viewModel;
    }


}