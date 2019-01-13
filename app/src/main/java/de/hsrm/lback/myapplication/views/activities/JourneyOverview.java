package de.hsrm.lback.myapplication.views.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.adapters.JourneyAdapter;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.viewmodels.JourneyViewModel;

public class JourneyOverview extends AppCompatActivity {
    private RecyclerView journeyListView;
    private JourneyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_overview);

        initJourneyListView();


        JourneyViewModel viewModel = ViewModelProviders.of(this).get(JourneyViewModel.class);


        // get src and target location
        int srcId = getIntent().getIntExtra(Location.SRC_LOCATION, 0);
        int targetId = getIntent().getIntExtra(Location.DESTINATION_LOCATION, 0);

        viewModel.init(srcId, targetId);

        viewModel.getSrcData().observe(this, viewModel::onSrcChange);
        viewModel.getTargetData().observe(this, viewModel::onTargetChange);

        viewModel.getJourneys().observe(this, this::onJourneysChange);
    }

    private void initJourneyListView() {
        journeyListView = findViewById(R.id.journeys_list_view);
        journeyListView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        journeyListView.setLayoutManager(layoutManager);

        adapter = new JourneyAdapter(new ArrayList<>());

        journeyListView.setAdapter(adapter);
    }


    private void onJourneysChange(List<Journey> journeys) {
        Log.d("journeys", journeys.toString());
        adapter.setJourneys(journeys);
    }
}
