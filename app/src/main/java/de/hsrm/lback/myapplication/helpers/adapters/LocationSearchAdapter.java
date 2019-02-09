package de.hsrm.lback.myapplication.helpers.adapters;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;
import de.hsrm.lback.myapplication.views.activities.LocationOverview;
import de.hsrm.lback.myapplication.views.views.LocationView;

public class LocationSearchAdapter extends BaseAdapter {

    private AppCompatActivity activity;
    private List<Location> locations;
    private View.OnClickListener listener;

    public LocationSearchAdapter(
            AppCompatActivity activity,
            List<Location> locations,
            View.OnClickListener listener) {
        super();
        this.locations = locations;
        this.listener = listener;
        this.activity = activity;
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

        Location location = locations.get(position);
        convertView = activity.getLayoutInflater().inflate(R.layout.location_list_layout, null);

        ((TextView)convertView).setText(location.getName());

        convertView.setTag(location.getApiId());

        convertView.setOnClickListener(listener);

        return convertView;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
