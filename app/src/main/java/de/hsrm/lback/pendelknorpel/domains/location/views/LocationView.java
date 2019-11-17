package de.hsrm.lback.pendelknorpel.domains.location.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.helpers.LocationDragShadowBuilder;
import de.hsrm.lback.pendelknorpel.helpers.ResourcesHelper;


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
        this.setOnTouchListener((v, e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.startDragAndDrop(null, new LocationDragShadowBuilder(v, getResources()), v, 0);
                    return true;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    return true;
                default:
                    return false;
            }
        });

        // set viewmodel to process the drop
        this.setOnDragListener(viewModel::onDrag);

        // subscribe to location changes in viewModel
        viewModel.setLocationUpdateCallback(this::onLocationUpdate);
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