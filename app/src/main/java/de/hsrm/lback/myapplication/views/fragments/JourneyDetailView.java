package de.hsrm.lback.myapplication.views.fragments;

import android.content.Context;
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

import com.google.gson.Gson;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.adapters.ConnectionsAdapter;
import de.hsrm.lback.myapplication.models.Journey;

/**
 */
public class JourneyDetailView extends Fragment {


    public JourneyDetailView() {
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
        return inflater.inflate(R.layout.fragment_journey_detail_view, container, false);
    }

    /** show journey information after view was created */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get journey from sharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.current_journey), Context.MODE_PRIVATE);

        String json = preferences.getString(Journey.JOURNEY_ID, "");

        Journey j = new Gson().fromJson(json, Journey.class);

        // display journey
        ListView connectionList = view.findViewById(R.id.connection_list);

        BaseAdapter adapter = new ConnectionsAdapter(j.getConnections());

        connectionList.setAdapter(adapter);

    }
}
