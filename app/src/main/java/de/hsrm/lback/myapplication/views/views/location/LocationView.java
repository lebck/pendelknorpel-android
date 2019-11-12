package de.hsrm.lback.myapplication.views.views.location;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.LocationDragShadowBuilder;
import de.hsrm.lback.myapplication.helpers.ResourcesHelper;
import de.hsrm.lback.myapplication.models.Location;


/**
 * Represents a single location on the LocationOverview
 */
public class LocationView extends View {

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
        this.setOnDragListener(viewModel::onDrag);

        // subscribe to location changes in viewModel
        viewModel.setLocationUpdateCallback(this::onLocationUpdate);
    }

    private void onLocationUpdate(@Nullable Location location) {
        if (location == null) return;

        // set logo and name
        if (location.getDisplayName() == null || location.getDisplayName().equals(""))
            this.setName(location.getName());
        else
            this.setName(location.getDisplayName());

        this.setLogo(location.getLogo());
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