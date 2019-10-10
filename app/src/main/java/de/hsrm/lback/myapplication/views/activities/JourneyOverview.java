package de.hsrm.lback.myapplication.views.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.adapters.JourneyAdapter;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.JourneyList;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.JourneyRepository;
import de.hsrm.lback.myapplication.viewmodels.JourneyViewModel;
import de.hsrm.lback.myapplication.views.fragments.DatePickerFragment;
import de.hsrm.lback.myapplication.views.fragments.TimePickerFragment;

/**
 * Display a list of journeys from a location to another location
 */
public class JourneyOverview extends AppCompatActivity {
    public static final String SRC_JSON = "src_json";
    public static final String TARGET_JSON = "target_json";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.");
    private RecyclerView journeyListView;
    private JourneyAdapter adapter;
    private ProgressBar progressBar;
    private Button timePickerButton;
    private Button nowButton;
    private Button dateButton;
    // ToDo add showPreviousButton
    private JourneyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_overview);
        progressBar = findViewById(R.id.progress_bar);
        timePickerButton = findViewById(R.id.time_picker_button);
        nowButton = findViewById(R.id.now_button);
        dateButton = findViewById(R.id.date_picker_button);

        initJourneyListView();

        viewModel = ViewModelProviders.of(this).get(JourneyViewModel.class);

        // get src and target location
        int srcId = getIntent().getIntExtra(Location.SRC_LOCATION, 0);
        int targetId = getIntent().getIntExtra(Location.DESTINATION_LOCATION, 0);

        viewModel.init();

        if (srcId == 0) viewModel.setSrc(getIntent().getStringExtra(SRC_JSON));
        else viewModel.setSrc(srcId);

        if (targetId == 0) viewModel.setTarget(getIntent().getStringExtra(TARGET_JSON));
        else viewModel.setTarget(targetId);

        // initialize observers
        viewModel.getSrcData().observe(this, viewModel::onSrcChange);
        viewModel.getTargetData().observe(this, viewModel::onTargetChange);
        viewModel.getJourneysData().observe(this, this::onJourneysChange);
        viewModel.getDateTimeData().observe(this, this::onDateTimeChange);
        viewModel.getReadyToLoadData().observe(this, this::onReadyToLoadChange);

        // initialize listeners
        timePickerButton.setOnClickListener(this::onTimePickerClicked);
        nowButton.setOnClickListener(this::onNowButtonClicked);
        dateButton.setOnClickListener(this::onDatePickerClicked);

    }

    /**
     * load new journeys when current dateTime changes and viewModel is ready to load,
     * display date and time in buttons
     */
    private void onDateTimeChange(LocalDateTime dateTime) {
        Boolean readyToStart = viewModel.getReadyToLoadData().getValue();
        if (readyToStart != null && readyToStart) {
            showProgressBar();
            viewModel.fetchJourneys();
        }
        timePickerButton.setText(dateTime.format(TIME_FORMAT));
        dateButton.setText(getDateString(dateTime.toLocalDate()));
    }

    private String getDateString(LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        String dateString = date.format(DATE_FORMAT);

        if (date.equals(today)) dateString = getString(R.string.today);
        else if (date.equals(tomorrow)) dateString = getString(R.string.tomorrow);

        return dateString;
    }

    /**
     * set up listView containing all journeys
     */
    private void initJourneyListView() {
        journeyListView = findViewById(R.id.journeys_list_view);
        journeyListView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        journeyListView.setLayoutManager(layoutManager);

        adapter = new JourneyAdapter(new JourneyList(Collections.emptyList(),"",""),
                this::onJourneyClick,
                this::onShowMoreClicked,
                this::onShowEarlierClicked);

        journeyListView.setAdapter(adapter);
    }

    /**
     * open specific view for journey
     */
    private void onJourneyClick(Journey journey) {
        // create intent
        Intent intent = new Intent(this, JourneyView.class);


        JourneyRepository.setCurrentJourney(getApplicationContext(), journey);
        // start journeyView activity
        startActivity(intent);
    }

    /**
     * Update all journeys and remove loading indicator and show more button
     */
    private void onJourneysChange(JourneyList journeys) {
        adapter.setJourneys(journeys);
        if (journeys.getJourneys().size() > 0) {
            this.hideProgressBar();
        }
    }

    /**
     * show progress bar and hide "show more" button
     */
    private void showProgressBar() {
        journeyListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide progress bar and show "show more" button
     */
    private void hideProgressBar() {
        journeyListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void onReadyToLoadChange(Boolean readyToLoad) {
        if (readyToLoad) viewModel.fetchJourneys();
    }

    private void onTimePickerClicked(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setReceiver(viewModel::onTimeChosen);
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    private void onDatePickerClicked(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setReceiver(viewModel::onDateChosen);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void onNowButtonClicked(View view) {
        viewModel.setDateTime(LocalDateTime.now());
    }

    private void onShowMoreClicked(View view) {
        viewModel.fetchMoreJourneys();
    }

    private void onShowEarlierClicked(View view) {
        viewModel.fetchEarlierJourneys();
    }
}
