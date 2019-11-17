package de.hsrm.lback.pendelknorpel.domains.journey.views.overview.components;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.helpers.BackgroundManager;
import de.hsrm.lback.pendelknorpel.domains.journey.models.Journey;
import de.hsrm.lback.pendelknorpel.services.JourneyService;
import de.hsrm.lback.pendelknorpel.domains.journey.views.detail.components.JourneyDetailFragment;

/**
 * Displays a small overview over the current journey
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
        onJourneyChange();

    }

    @Override
    public void onResume() {
        super.onResume();
        onJourneyChange();
    }

    /**
     * update view according to journey
     */
    private void onJourneyChange() {
        Journey j = null;

        if (getContext() != null)
            j = JourneyService.getCurrentJourney(getContext());

        if (j != null && getView() != null) {
            // display journey
            TextView journeyDisplay = getView().findViewById(R.id.journey_text);

            journeyDisplay.setText(j.getDetailString());

            getView().setOnClickListener(this::onClick);
        }

    }

    /**
     * open Journey DetailView on click with animation
     * @param view
     */
    private void onClick(View view) {
        JourneyDetailFragment journeyDetailFragment = new JourneyDetailFragment();

        journeyDetailFragment.setBackground(
                BackgroundManager.getBlurryBackground(getActivity().findViewById(R.id.location_overview_root))
        );

        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.location_overview_root, journeyDetailFragment)
                .addToBackStack(null);

        transaction.commit();
    }
}
