package de.hsrm.lback.pendelknorpel.helpers.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.domains.location.views.LocationView;
import de.hsrm.lback.pendelknorpel.domains.location.views.LocationViewModel;
import de.hsrm.lback.pendelknorpel.domains.location.views.overview.LocationOverviewStateMachine;

public class LocationAdapter extends BaseAdapter {

    private List<Location> locations;
    private LocationOverviewStateMachine stateMachine;
    private LayoutInflater inflater;

    public LocationAdapter(
            List<Location> locations, LocationOverviewStateMachine stateMachine, LayoutInflater inflater) {
        super();
        this.locations = locations;
        this.stateMachine = stateMachine;
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
        Location location = locations.get(position);
        return location != null ? location.hashCode() : 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LocationViewModel viewModel = new LocationViewModel(stateMachine);
        viewModel.init(locations.get(position));
        convertView = inflater.inflate(R.layout.component_location_layout, null);

        if (convertView instanceof LocationView)
            ((LocationView) convertView).init(viewModel);


        return convertView;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
