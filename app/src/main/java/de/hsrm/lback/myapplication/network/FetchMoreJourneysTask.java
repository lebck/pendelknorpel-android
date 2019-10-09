package de.hsrm.lback.myapplication.network;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.time.LocalDateTime;
import java.util.List;

import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.JourneyList;
import de.hsrm.lback.myapplication.models.Location;

/**
 * Task that fetches journeys from given start and end location
 */
public class FetchMoreJourneysTask extends AsyncTask<Location, Void, JourneyList> {
    private MutableLiveData<JourneyList> journeysData;
    private LocalDateTime dateTime;
    private JourneyList currentJourneys;

    public FetchMoreJourneysTask(JourneyList currentJourneys, MutableLiveData<JourneyList> journeysData, LocalDateTime dateTime) {
        this.journeysData = journeysData;
        this.dateTime = dateTime;
        this.currentJourneys = currentJourneys;
    }

    @Override
    protected JourneyList doInBackground(Location ...locations) {
        ApiConnector connector = new ApiConnector();

        Location src = locations[0];
        Location target = locations[1];

        JourneyList moreJourneys = connector.getMore(src, target, currentJourneys.getScrollForwardData(), dateTime);

        String newGetMoreData = moreJourneys.getScrollForwardData();

        List<Journey> aggregatedJourneyList = currentJourneys.getJourneys();
        aggregatedJourneyList.addAll(moreJourneys.getJourneys());

        JourneyList newJourneys = new JourneyList(aggregatedJourneyList, newGetMoreData, currentJourneys.getScrollBackwardsData());

        journeysData.postValue(newJourneys);

        return moreJourneys;
    }
}
