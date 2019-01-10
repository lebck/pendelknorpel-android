package de.hsrm.lback.myapplication.views.views;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import de.hsrm.lback.myapplication.helpers.LocationDragShadowBuilder;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;
import de.hsrm.lback.myapplication.views.activities.EditLocationView;


/**
 * Represents a single location
 *
 */
public class LocationView extends android.support.v7.widget.AppCompatTextView {

    private Paint textPaint;
    private LocationViewModel viewModel;
    private Activity activity;

    public LocationView(AppCompatActivity activity, LocationViewModel viewModel) {
        super(activity);
        this.viewModel = viewModel;
        this.textPaint = new Paint();
        this.activity = activity;

        setTextSize(20f);
        setPadding(20, 20, 20, 20);

        this.setTag("location_view");

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
        this.setOnDragListener(viewModel);

        // set binding to name
        viewModel.getLocation().getName().observe(activity, this::onNameChanged);

    }

    private void onNameChanged(String name) {
        this.setText(name);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
                String.format("Journey from %s to %s", src.getName().getValue(), target.getName().getValue()),
                Snackbar.LENGTH_SHORT
        );

        snackbar.show();
    }

    public LocationViewModel getViewModel() {
        return viewModel;
    }

    /**
     * open view to edit the location
     */
    public void openEditView() {
        Intent intent = new Intent(activity, EditLocationView.class);

        intent.putExtra(LocationRepository.LOCATION_UID, this.viewModel.getLocation().getUid());

        activity.startActivity(intent);
    }
}