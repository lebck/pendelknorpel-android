package de.hsrm.lback.myapplication.persistence;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.TypeConverter;

public class StringLiveDataConverter {
    @TypeConverter
    public static MutableLiveData<String> fromString(String s) {
        MutableLiveData<String> m = new MutableLiveData<>();
        m.postValue(s);
        return m;
    }

    @TypeConverter
    public static String fromMutableLiveData(MutableLiveData<String> m) {
        return m.getValue();
    }
}
