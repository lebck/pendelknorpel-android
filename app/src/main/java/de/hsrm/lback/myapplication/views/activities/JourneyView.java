package de.hsrm.lback.myapplication.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.models.Journey;

public class JourneyView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_view);

        Journey j = getIntent().getParcelableExtra("j");

        Log.d("", j.toString());
    }
}
