package de.hsrm.lback.myapplication.helpers.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Connection;
import de.hsrm.lback.myapplication.models.Journey;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder> {
    private List<Journey> journeys;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemView;
        public ViewHolder(@NonNull LinearLayout itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public JourneyAdapter(List<Journey> journeys) {
        this.journeys = journeys;
    }

    @NonNull
    @Override
    public JourneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        LinearLayout layout =
                (LinearLayout) inflater.inflate(R.layout.journey_view, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyAdapter.ViewHolder viewHolder, int i) {
        Journey j = journeys.get(i);
        LinearLayout layout = viewHolder.itemView;
        TextView startTime = layout.findViewById(R.id.start_time);
        TextView endTime = layout.findViewById(R.id.end_time);
        TextView changes = layout.findViewById(R.id.changes);
        TextView vehicle = layout.findViewById(R.id.vehicle);

        List<Connection> connections = j.getConnections();

        startTime.setText(connections.get(0).getStartTime().format(Connection.FORMATTER));
        endTime.setText(connections.get(connections.size() - 1).getEndTime().format(Connection.FORMATTER));
        changes.setText(String.format("U: %s", connections.size()));
        vehicle.setText(j.getVehicleString());
    }

    @Override
    public int getItemCount() {
        return journeys.size();
    }

    public void setJourneys(List<Journey> journeys) {
        this.journeys = journeys;
        notifyDataSetChanged();
    }
}
