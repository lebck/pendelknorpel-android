package de.hsrm.lback.myapplication.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheManager {

    private static final String BITMAP_NAME = "bg.bmp";

    public static void writeBackgroundToCache(Context context, View view) {
        try {
            File bitmap = File.createTempFile(BITMAP_NAME, null, context.getCacheDir());

            Bitmap bitmapData = BackgroundManager.getBlurryBackground(view);

            FileOutputStream stream = new FileOutputStream(bitmap);

            bitmapData.compress(Bitmap.CompressFormat.PNG, 1, stream);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static Bitmap getBackground(Context context) {
        File file = new File(context.getCacheDir() + BITMAP_NAME);

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;

    }
}
