package de.hsrm.lback.myapplication.helpers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Connection;

public class ConnectionsAdapter extends BaseAdapter {
    private List<Connection> connections;

    public ConnectionsAdapter(List<Connection> connections) {
        this.connections = connections;
    }

    @Override
    public int getCount() {
        return connections.size();
    }

    @Override
    public Object getItem(int position) {
        return connections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return connections.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        convertView = inflater.inflate(R.layout.connection_view, null);

        Connection connection = connections.get(position);

        TextView startLocationName = convertView.findViewById(R.id.start_location_name);
        TextView endLocationName = convertView.findViewById(R.id.end_location_name);
        TextView lineName = convertView.findViewById(R.id.vehicle_id);
        TextView startTime = convertView.findViewById(R.id.start_time);
        TextView endTime = convertView.findViewById(R.id.end_time);

        startLocationName.setText(connection.getStartLocation().getName());
        endLocationName.setText(connection.getEndLocation().getName());
        startTime.setText(connection.getStartTimeString());
        endTime.setText(connection.getEndTimeString());

        lineName.setText(String.format("%s %s",
                connection.getVehicle(), connection.getLineId()
        ));

        return convertView;
    }
}
