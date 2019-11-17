package de.hsrm.lback.pendelknorpel.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class BackgroundManager {
    public static final String BACKGROUND = "background";

    private RenderScript rs;

    private static final boolean IS_BLUR_SUPPORTED = Build.VERSION.SDK_INT >= 17;
    private static final int MAX_RADIUS = 25;

    private BackgroundManager(RenderScript rs) {
        this.rs = rs;
    }

    @Nullable
    private Bitmap blur(@NonNull Bitmap bitmap, float radius, int repeat) {

        if (!IS_BLUR_SUPPORTED) {
            return null;
        }

        if (radius > MAX_RADIUS) {
            radius = MAX_RADIUS;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Create allocation type
        Type bitmapType = new Type.Builder(rs, Element.RGBA_8888(rs))
                .setX(width)
                .setY(height)
                .setMipmaps(false) // We are using MipmapControl.MIPMAP_NONE
                .create();

        // Create allocation
        Allocation allocation = Allocation.createTyped(rs, bitmapType);

        // Create blur script
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));


        blurScript.setRadius(radius);

        // Copy data to allocation
        allocation.copyFrom(bitmap);

        // set blur script input
        blurScript.setInput(allocation);

        // invoke the script to blur
        blurScript.forEach(allocation);

        // Repeat the blur for extra effect
        for (int i=0; i<repeat; i++) {
            blurScript.forEach(allocation);
        }

        // copy data back to the bitmap
        allocation.copyTo(bitmap);

        // release memory
        allocation.destroy();
        blurScript.destroy();
        allocation = null;
        blurScript = null;

        return bitmap;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

    public static Bitmap getBlurryBackground(View view) {
        Bitmap viewBitmap = getBitmapFromView(view);

        BackgroundManager processor = new BackgroundManager(RenderScript.create(view.getContext()));

        return processor.blur(viewBitmap, 25, 3);
    }

}
