package de.hsrm.lback.pendelknorpel.domains.location.views.edit.components.search;

import android.arch.lifecycle.LiveData;
import android.text.TextWatcher;

import java.util.List;

import de.hsrm.lback.pendelknorpel.domains.location.models.Location;

public interface SearchLocationViewModel extends TextWatcher {
    void setLocationByIndex(int index);

    LiveData<List<Location>> getLocationResults();

    LiveData<Location> getLocationData();

    void cleanUp();
}
