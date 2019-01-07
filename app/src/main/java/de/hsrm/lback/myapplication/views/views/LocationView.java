package de.hsrm.lback.myapplication.views.views;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.LocationDragShadowBuilder;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;


/**
 * Represents a single location
 *
 */
public class LocationView extends android.support.v7.widget.AppCompatTextView {

    private Paint textPaint;
    private LocationViewModel model;
    private Activity activity;

    public LocationView(AppCompatActivity activity, LocationViewModel model) {
        super(activity);
        this.model = model;
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
        this.setOnDragListener(model);

        // set binding to name
        model.getName().observe(activity, this::onNameChanged);

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

    public LocationViewModel getModel() {
        return model;
    }

    /**
     * show dialog to change the name of a location
     */
    public void showChangeName() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View box = inflater.inflate(R.layout.change_name_box, null);

        AlertDialog changeNameDialog =
                new AlertDialog.Builder(activity)
                    .setView(box)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        // change name of location
                        String newName = ((EditText)box.findViewById(R.id.name)).getText().toString();
                        this.model.setName(newName);
                    })
                    .create();
        changeNameDialog.show();

    }
}
