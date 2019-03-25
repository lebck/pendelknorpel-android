package de.hsrm.lback.myapplication.helpers;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ResourcesHelper {
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }

    public static List<String> getLogoList() {
        // TODO return dynamically generated list
        return Arrays.asList(
                "icon_check",
                "icon_home",
                "icon_money",
                "icon_umbrella",
                "icon_trash"
        );
    }
}
