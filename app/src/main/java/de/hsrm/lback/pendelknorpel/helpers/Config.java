package de.hsrm.lback.pendelknorpel.helpers;


import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.hsrm.lback.pendelknorpel.R;

public class Config {
    private Properties properties;
    public Config(Resources resources) {
        InputStream rawResource = resources.openRawResource(R.raw.config);
        properties = new Properties();

        try {
            properties.load(rawResource);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getValue(String name) {
        return properties.getProperty(name);
    }
}
