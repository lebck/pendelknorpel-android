package de.hsrm.lback.myapplication.helpers.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import de.hsrm.lback.myapplication.R;
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
        // TODO set all data
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
