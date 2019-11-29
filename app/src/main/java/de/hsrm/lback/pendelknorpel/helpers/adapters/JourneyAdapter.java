package de.hsrm.lback.pendelknorpel.helpers.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.domains.journey.models.Connection;
import de.hsrm.lback.pendelknorpel.domains.journey.models.Journey;
import de.hsrm.lback.pendelknorpel.domains.journey.models.JourneyList;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder> {
    public interface JourneyClickListener {
        void onClick(Journey journey);
    }

    private JourneyList journeys;
    private JourneyClickListener listener;
    private View.OnClickListener onShowMoreListener;
    private View.OnClickListener onShowEarlierListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public JourneyAdapter(
            JourneyList journeys,
            JourneyClickListener listener,
            View.OnClickListener onShowMoreListener,
            View.OnClickListener onShowEarlierListener) {
        this.journeys = journeys;
        this.listener = listener;
        this.onShowMoreListener = onShowMoreListener;
        this.onShowEarlierListener = onShowEarlierListener;
    }

    @NonNull
    @Override
    public JourneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View layout = inflater.inflate(R.layout.component_journey_list_view, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyAdapter.ViewHolder viewHolder, int i) {
        Journey j = journeys.getJourneys().get(i);
        View layout = viewHolder.itemView;
        TextView startTime = layout.findViewById(R.id.start_time);
        TextView realStartTime = layout.findViewById(R.id.real_start_time);
        TextView endTime = layout.findViewById(R.id.end_time);
        TextView realEndTime = layout.findViewById(R.id.real_end_time);
        TextView changes = layout.findViewById(R.id.changes);
        TextView vehicle = layout.findViewById(R.id.vehicle);
        Button showMoreButton = layout.findViewById(R.id.more_journeys);
        ProgressBar showMoreProgressBar = layout.findViewById(R.id.show_more_progress);
        Button earlierButton = layout.findViewById(R.id.earlier_journeys);
        ProgressBar showEarlierProgressBar = layout.findViewById(R.id.earlier_progress);

        layout.setOnClickListener(v -> this.listener.onClick(j));

        List<Connection> connections = j.getConnections();

        String[] startTimeStrings = connections.get(0).getStartTimeString();
        String[] endTimeStrings = connections.get(connections.size() - 1).getEndTimeString();

        startTime.setText(startTimeStrings[0]);
        if (startTimeStrings[1].equals(""))
            realStartTime.setVisibility(View.GONE);
        else
            realStartTime.setText(startTimeStrings[1]);
        endTime.setText(endTimeStrings[0]);
        if (startTimeStrings[1].equals(""))
            realEndTime.setVisibility(View.GONE);
        else
            realEndTime.setText(endTimeStrings[1]);
        changes.setText(String.format("U: %s", connections.size() - 1));
        vehicle.setText(j.getVehicleString());

        if (i == 0) {
            // show "earlier" button & attach listener if this is the first item in the list
            earlierButton.setVisibility(View.VISIBLE);
            showEarlierProgressBar.setVisibility(View.GONE);
            // show spinner & hide button on click & then execute callback
            earlierButton.setOnClickListener(view -> {
                showEarlierProgressBar.setVisibility(View.VISIBLE);
                earlierButton.setVisibility(View.GONE);
                this.onShowEarlierListener.onClick(view);
            });

        } else if (i == journeys.getJourneys().size() - 1) {
            // show "show more" button & attach listener if this is the last item in the list
            showMoreButton.setVisibility(View.VISIBLE);
            // show spinner & hide button on click & then execute callback
            showMoreButton.setOnClickListener(view -> {
                showMoreProgressBar.setVisibility(View.VISIBLE);
                showMoreButton.setVisibility(View.GONE);
                this.onShowMoreListener.onClick(view);
            });
        } else {
            showMoreButton.setVisibility(View.GONE);
            showMoreProgressBar.setVisibility(View.GONE);
            showMoreButton.setOnClickListener(null);

            showEarlierProgressBar.setVisibility(View.GONE);
            earlierButton.setVisibility(View.GONE);
            earlierButton.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return journeys.getJourneys().size();
    }

    public void setJourneys(JourneyList journeys) {
        this.journeys = journeys;
        notifyDataSetChanged();
    }
}
