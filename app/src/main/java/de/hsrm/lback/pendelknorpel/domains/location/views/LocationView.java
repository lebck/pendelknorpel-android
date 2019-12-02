package de.hsrm.lback.pendelknorpel.domains.location.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.util.LocationDragShadowBuilder;
import de.hsrm.lback.pendelknorpel.util.ResourcesHelper;


/**
 * Represents a single location on the LocationOverviewActivity
 */
public class LocationView extends LinearLayout {
    private static final String DEFAULT_LOGO = "plus";
    private static final String GPS_LOGO = "gps";
    private static final String ANONYMOUS_LOGO = "plus";

    private LocationViewModel viewModel;
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

    /**
     * Setup ViewModel and DragDrop listeners
     */
    public void init(LocationViewModel viewModel) {
        this.viewModel = viewModel;
        this.locationNameView = findViewById(R.id.location_name);
        this.locationLogoView = findViewById(R.id.location_logo);

        this.setTag("component_connection_view");

        // start drag and drop instantly when view is touched

        this.setOnTouchListener(this::onTouch);

        // set viewmodel to process the drop
        this.setOnDragListener(this::onDrag);

        // subscribe to location changes in viewModel
        viewModel.setLocationUpdateCallback(this::onLocationUpdate);
    }

    private boolean onTouch(View v, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.animate()
                        .scaleX(1.2f)
                        .scaleY(1.2f)
                        .setDuration(200)
                        .start();

                v.startDragAndDrop(null, new LocationDragShadowBuilder(v, getResources()), v, 0);
                return true;
            case MotionEvent.ACTION_UP:
                v.animate()
                        .scaleX(1.2f)
                        .scaleY(1.2f)
                        .setDuration(200)
                        .start();
                v.performClick();
                return true;
            default:
                return false;
        }
    }

    private boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case (DragEvent.ACTION_DROP):
                LocationView src = (LocationView) event.getLocalState();
                LocationView target = (LocationView) v;

                src.animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(200)
                        .start();

                target.animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(200)
                        .start();

                LocationViewModel srcViewModel = src.getViewModel();
                LocationViewModel targetViewModel = target.getViewModel();

                viewModel.onSrcAndTargetChosen(srcViewModel, targetViewModel);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (!viewModel.isEmpty())
                    v.animate()
                            .scaleX(1.2f)
                            .scaleY(1.2f)
                            .setDuration(200)
                            .start();
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                if (!event.getLocalState().equals(this))
                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200)
                        .start();
                break;

        }

        return true;  // consume event
    }

    private void onLocationUpdate(@Nullable Location location) {
        // set edit style for empty locations unless this is anonymous or gps location view
        if (location == null) {
            if (this.viewModel.isAnonymous()) {
                setAnonymousStyle();
            } else if (this.viewModel.isGps()) {
                setGpsStyle();
            } else {
                setCreateNewStyle();
            }

            return;
        }


        // set logo and name
        if (location.getDisplayName() == null || location.getDisplayName().equals(""))
            this.setName(location.getName());
        else
            this.setName(location.getDisplayName());

        this.setLogo(location.getLogo());
    }

    private void setCreateNewStyle() {
        setLogo(DEFAULT_LOGO);
        setName("");
    }

    private void setGpsStyle() {
        setLogo(GPS_LOGO);
        setName(getResources().getString(R.string.gps));
    }

    private void setAnonymousStyle() {
        setLogo(ANONYMOUS_LOGO);
        setName(getResources().getString(R.string.anonymous));
    }

    private void setLogo(String s) {

        int resId = ResourcesHelper.getResId(s, R.drawable.class);
        if (resId > 0) this.locationLogoView.setImageResource(resId);

    }

    private void setName(String name) {
        this.locationNameView.setText(name);

        // make textview invisible if no name is set
        if (name.trim().equals(""))
            this.locationNameView.setVisibility(GONE);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public LocationViewModel getViewModel() {
        return viewModel;
    }


}