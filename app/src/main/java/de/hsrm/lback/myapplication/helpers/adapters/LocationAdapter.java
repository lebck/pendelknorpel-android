package de.hsrm.lback.myapplication.helpers.adapters;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.domains.location.models.Location;
import de.hsrm.lback.myapplication.domains.location.views.overview.LocationOverviewActivity;
import de.hsrm.lback.myapplication.domains.location.views.LocationViewModel;
import de.hsrm.lback.myapplication.domains.location.views.LocationView;

public class LocationAdapter extends BaseAdapter {

    private LocationOverviewActivity activity;
    private List<Location> locations;
    private Application application;

    public LocationAdapter(
            LocationOverviewActivity activity,
            List<Location> locations,
            Application application) {
        super();
        this.activity = activity;
        this.locations = locations;
        this.application = application;
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

        LocationViewModel viewModel = new LocationViewModel();
        viewModel.init(locations.get(position), this.activity);
        convertView = activity
                .getLayoutInflater()
                .inflate(R.layout.component_location_layout, null);

        if (convertView instanceof LocationView)
            ((LocationView)convertView).init(viewModel);


        return convertView;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
