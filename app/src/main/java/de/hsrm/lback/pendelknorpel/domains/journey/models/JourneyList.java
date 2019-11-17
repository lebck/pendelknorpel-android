package de.hsrm.lback.pendelknorpel.domains.journey.models;

import java.util.List;

public class JourneyList {
    private List<Journey> journeys;
    private String scrollForwardData;
    private String scrollBackwardsData;

    public JourneyList(List<Journey> journeys, String scrollForwardData, String scrollBackwardsData) {
        this.journeys = journeys;

        this.scrollForwardData = scrollForwardData;
        this.scrollBackwardsData = scrollBackwardsData;
    }


    public List<Journey> getJourneys() {
        return journeys;
    }

    public String getScrollForwardData() {
        return scrollForwardData;
    }

    public String getScrollBackwardsData() {
        return scrollBackwardsData;
    }
}
