package de.hsrm.lback.myapplication.views.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.adapters.ConnectionsAdapter;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.viewmodels.JourneyDetailViewModel;

/**
 * Display all information about a journey
 */
public class JourneyDetailView extends Fragment {
    private Bitmap background;
    private ConnectionsAdapter connectionsAdapter;
    private JourneyDetailViewModel viewModel;
    private SwipeRefreshLayout view;

    public JourneyDetailView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(JourneyDetailViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journey_detail_view, container, false);
    }

    /** show journey information after view was created */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (background != null)
            view.setBackground(new BitmapDrawable(getResources(), background));

        this.view = (SwipeRefreshLayout) view;
        this.view.setOnRefreshListener(this::handleRefresh);
        this.viewModel.getJourneyData().observe(this, this::onJourneyChanged);


    }

    private void onJourneyChanged(Journey journey) {
        // set headline
        TextView journeyHeadline = view.findViewById(R.id.connection_headline);
        journeyHeadline.setText(String.format(
                "VERBINDUNG\n%s", journey.getDetailString()
        ));

        // display journey
        ListView connectionList = view.findViewById(R.id.connection_list);

        this.connectionsAdapter = new ConnectionsAdapter(journey.getConnections());

        connectionList.setAdapter(this.connectionsAdapter);

        this.view.setRefreshing(false);

    }

    private void handleRefresh() {
        this.viewModel.refreshJourney();
    }



    public void setBackground(Bitmap bitmap) {
        this.background = bitmap;
    }
}
