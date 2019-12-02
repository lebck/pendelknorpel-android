package de.hsrm.lback.pendelknorpel.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

public class WindowService {
    private Display display;
    private Point size;
    private DisplayMetrics displayMetrics;

    public WindowService(Context context) {
        android.view.WindowManager wm = (android.view.WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.display = wm.getDefaultDisplay();
        this.init();
    }

    public WindowService(Activity activity) {
        this.display = activity.getWindowManager().getDefaultDisplay();
        this.init();
    }

    private void init() {
        this.size = new Point();
        displayMetrics = new DisplayMetrics();
        this.display.getSize(size);
        display.getMetrics(displayMetrics);
    }


    public int getWidth() {
        return this.size.x;
    }

    public int getHeight() {
        return this.size.y;
    }

    public float dpFromPx(float px) {
        return px / displayMetrics.density;
    }

    public float pxFromDp(float dp) {
        return dp * displayMetrics.density;
    }

    public int calculateMeasures(int value) {
        int width = getWidth();

        return (int) value * width / 1080;


    }
}
