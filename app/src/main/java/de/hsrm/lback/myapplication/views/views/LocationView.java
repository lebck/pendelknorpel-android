package de.hsrm.lback.myapplication.views.views;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

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

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void showDropSnackbar(Location src, Location target) {
        Snackbar snackbar = Snackbar.make(
                this,
                String.format("Journey from %s to %s", src.getName(), target.getName()),
                Snackbar.LENGTH_SHORT
        );

        snackbar.show();
    }

    public LocationViewModel getModel() {
        return model;
    }
}
