package de.hsrm.lback.myapplication.helpers.adapters;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;
import de.hsrm.lback.myapplication.views.activities.LocationOverview;
import de.hsrm.lback.myapplication.views.views.LocationView;

public class LocationAdapter extends BaseAdapter {

    private LocationOverview activity;
    private List<Location> locations;
    private LocationRepository locationRepository;
    private Application application;

    public LocationAdapter(LocationOverview activity, List<Location> locations, LocationRepository locationRepository, Application application) {
        super();
        this.activity = activity;
        this.locations = locations;
        this.locationRepository = locationRepository;
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

        LocationViewModel viewModel = new LocationViewModel(application);
        viewModel.init(locations.get(position));
        convertView = activity.getLayoutInflater().inflate(R.layout.location_layout, null);

        ((LocationView)convertView).init(activity, viewModel);

        return convertView;
    }


}
