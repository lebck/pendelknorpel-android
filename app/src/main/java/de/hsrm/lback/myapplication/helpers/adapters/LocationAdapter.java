package de.hsrm.lback.myapplication.helpers.adapters;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.services.LocationService;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;
import de.hsrm.lback.myapplication.views.activities.LocationOverview;
import de.hsrm.lback.myapplication.views.views.LocationView;

public class LocationAdapter extends BaseAdapter {

    private LocationOverview activity;
    private List<Location> locations;
    private LocationService locationService;
    private Application application;
    private int layoutId;

    public LocationAdapter(
            LocationOverview activity,
            List<Location> locations,
            LocationService locationService,
            Application application,
            int layoutId) {
        super();
        this.activity = activity;
        this.locations = locations;
        this.locationService = locationService;
        this.application = application;
        this.layoutId = layoutId;
    }


    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return locations.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LocationViewModel viewModel = new LocationViewModel(application);
        viewModel.init(locations.get(position), this.activity);
        convertView = activity.getLayoutInflater().inflate(layoutId, null);

        if (convertView instanceof LocationView)
            ((LocationView)convertView).init(viewModel);


        return convertView;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
