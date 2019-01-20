package de.hsrm.lback.myapplication.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.adapters.ConnectionsAdapter;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.repositories.JourneyRepository;
import de.hsrm.lback.myapplication.views.activities.JourneyView;

/**
 */
public class JourneyViewSmall extends Fragment {


    public JourneyViewSmall() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journey_view_small, container, false);
    }

    /** show journey information after view was created */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get current journey
        Journey j = JourneyRepository.getCurrentJourney(getContext());
        if (j != null) {
            // display journey
            TextView journeyDisplay = view.findViewById(R.id.journey_text);

            journeyDisplay.setText(j.getDetailString());

            view.setOnClickListener(this::onClick);
        }

    }

    private void onClick(View view) {
        Intent intent = new Intent(getContext(), JourneyView.class);

        getActivity().startActivity(intent);
    }
}
