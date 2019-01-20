package de.hsrm.lback.myapplication.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.adapters.ConnectionsAdapter;
import de.hsrm.lback.myapplication.models.Journey;

public class JourneyView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_view);
    }


}
