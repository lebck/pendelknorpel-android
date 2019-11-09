package de.hsrm.lback.myapplication.helpers.tasks;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.time.LocalDateTime;
import java.util.List;

import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.JourneyList;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.network.ApiConnector;

/**
 * Task that fetches journeys from given start and end location
 */
public class FetchMoreJourneysTask extends AsyncTask<Location, Void, JourneyList> {
    private MutableLiveData<JourneyList> journeysData;
    private LocalDateTime dateTime;
    private JourneyList currentJourneys;
    private boolean more;

    public FetchMoreJourneysTask(JourneyList currentJourneys, MutableLiveData<JourneyList> journeysData, LocalDateTime dateTime, boolean more) {
        this.journeysData = journeysData;
        this.dateTime = dateTime;
        this.currentJourneys = currentJourneys;
        this.more = more;
    }

    @Override
    protected JourneyList doInBackground(Location... locations) {
        ApiConnector connector = new ApiConnector();

        Location src = locations[0];
        Location target = locations[1];

        String moreData = more ? currentJourneys.getScrollForwardData() : null;
        String earlierData = more ? null : currentJourneys.getScrollBackwardsData();

        JourneyList moreJourneys = connector.getMore(src, target, moreData, earlierData, dateTime);


        List<Journey> aggregatedJourneyList = currentJourneys.getJourneys();
        JourneyList newJourneys;

        if (more) {
            String newGetMoreData = moreJourneys.getScrollForwardData();
            aggregatedJourneyList.addAll(moreJourneys.getJourneys());
            newJourneys = new JourneyList(aggregatedJourneyList, newGetMoreData, currentJourneys.getScrollBackwardsData());
        } else {
            String getEarlierData = moreJourneys.getScrollBackwardsData();
            aggregatedJourneyList.addAll(0, moreJourneys.getJourneys());
            newJourneys = new JourneyList(aggregatedJourneyList, currentJourneys.getScrollForwardData(), getEarlierData);
        }


        journeysData.postValue(newJourneys);

        return moreJourneys;
    }
}
