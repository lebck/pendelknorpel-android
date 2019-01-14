package de.hsrm.lback.myapplication.helpers.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

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

        startLocationName.setText(String.format(
                "%s\t%s",
                connection.getStartTimeString(),
                connection.getStartLocation().getName().getValue()
        ));
        endLocationName.setText(
                String.format("%s\t%s",
                        connection.getEndTimeString(),
                        connection.getEndLocation().getName().getValue()
        ));

        lineName.setText(String.format("%s %s",
                connection.getVehicle(), connection.getLineId()
        ));

        return convertView;
    }
}
