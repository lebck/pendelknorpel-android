package de.hsrm.lback.myapplication.helpers;

import java.lang.reflect.Field;

public class ResourcesHelper {
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }
}
