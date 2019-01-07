package de.hsrm.lback.myapplication.views.views;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipDescription;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;

public class LocationView extends android.support.v7.widget.AppCompatTextView {

    private Paint textPaint;
    private LocationViewModel model;

    public LocationView(AppCompatActivity activity, LocationViewModel model) {
        super(activity);
        this.model = model;
        this.textPaint = new Paint();

        setTextSize(20f);
        setPadding(20, 20, 20, 20);

        this.setTag("location_view");

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

        this.setOnDragListener(model);

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
}
