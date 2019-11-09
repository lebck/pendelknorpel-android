package de.hsrm.lback.myapplication.helpers.adapters;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Location;

public class LocationSearchAdapter extends BaseAdapter {

    private AppCompatActivity activity;
    private List<Location> locations;
    private View.OnClickListener listener;

    public LocationSearchAdapter(
            AppCompatActivity activity,
            View.OnClickListener listener) {
        super();
        this.locations = Collections.emptyList();
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
        notifyDataSetChanged();
    }
}
