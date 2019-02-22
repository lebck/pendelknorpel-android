package de.hsrm.lback.myapplication.helpers.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Connection;
import de.hsrm.lback.myapplication.models.Journey;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder> {
    private List<Journey> journeys;
    private JourneyClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout itemView;
        public ViewHolder(@NonNull RelativeLayout itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public interface JourneyClickListener {
        void onClick(Journey journey);
    }

    public JourneyAdapter(List<Journey> journeys, JourneyClickListener listener) {
        this.journeys = journeys;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JourneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        RelativeLayout layout =
                (RelativeLayout) inflater.inflate(R.layout.journey_list_view, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyAdapter.ViewHolder viewHolder, int i) {
        Journey j = journeys.get(i);
        RelativeLayout layout = viewHolder.itemView;
        TextView startTime = layout.findViewById(R.id.start_time);
        TextView realStartTime = layout.findViewById(R.id.real_start_time);
        TextView endTime = layout.findViewById(R.id.end_time);
        TextView realEndTime = layout.findViewById(R.id.real_end_time);
        TextView changes = layout.findViewById(R.id.changes);
        TextView vehicle = layout.findViewById(R.id.vehicle);

        layout.setOnClickListener(v -> this.listener.onClick(j));

        List<Connection> connections = j.getConnections();

        String[] startTimeStrings = connections.get(0).getStartTimeString();
        String[] endTimeStrings = connections.get(connections.size() - 1).getEndTimeString();

        startTime.setText(startTimeStrings[0]);
        realStartTime.setText(startTimeStrings[1]);
        endTime.setText(endTimeStrings[0]);
        realEndTime.setText(endTimeStrings[1]);
        changes.setText(String.format("U: %s", connections.size() - 1));
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
