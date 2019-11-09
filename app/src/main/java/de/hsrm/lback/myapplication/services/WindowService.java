package de.hsrm.lback.myapplication.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class WindowService {
    private Display display;
    private Point size;

    public WindowService(Context context) {
        this.size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.display = wm.getDefaultDisplay();
        this.display.getSize(size);
    }
    public WindowService(Activity activity) {
        this.size = new Point();
        this.display = activity.getWindowManager().getDefaultDisplay();
        this.display.getSize(size);
    }


    public int getWidth() {
        return this.size.x;
    }

    public int getHeight() {
        return this.size.y;
    }
}
