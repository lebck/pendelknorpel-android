package de.hsrm.lback.myapplication.helpers.adapters;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.domains.location.models.Location;

public class LocationSearchAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Location> locations;
    private View.OnClickListener listener;

    public LocationSearchAdapter(
            LayoutInflater inflater,
            View.OnClickListener listener) {
        super();
        this.locations = Collections.emptyList();
        this.listener = listener;
        this.inflater = inflater;
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
        convertView = this.inflater.inflate(R.layout.component_location_list_layout, null);

        ((TextView)convertView).setText(location.getName());

        convertView.setTag(location.getApiId());
        convertView.setTag(R.id.location_index, position);

        convertView.setOnClickListener(listener);

        return convertView;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }
}
