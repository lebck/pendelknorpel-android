package de.hsrm.lback.pendelknorpel.network;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import de.hsrm.lback.pendelknorpel.domains.journey.models.Journey;
import de.hsrm.lback.pendelknorpel.domains.journey.models.JourneyList;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.util.Config;

/**
 * Controls connection to apis (currently only RMV API)
 */
public class ApiConnector {
    private static final String BASE_URL = "/hapi";
    private static final String DOMAIN = "https://www.rmv.de";


    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:MM");

    private XMLParser parser;
    private String journeyPath;
    private String locationPath;
    private String gpsPath;


    public ApiConnector(Resources resources) {
        parser = new XMLParser();
        String apiKey = new Config(resources).getValue("api_key");
        String authPath = String.format("?accessId=%s", apiKey);
        String urlPattern = DOMAIN + BASE_URL + "%s" + authPath;

        locationPath = String.format(urlPattern, "/location.name");
        journeyPath = String.format(urlPattern, "/trip");
        gpsPath = String.format(urlPattern, "/location.nearbystops");
    }

    /**
     * return List of journeys that match the start and end location and the time
     */
    public JourneyList getDepartures(Location from, Location to, LocalDateTime time) {
        String fromId = from.getApiId();
        String toId = to.getApiId();
        String xml = "";
        try {
            URL url = new URL(
                    journeyPath +
                            "&originExtId=" + fromId +
                            "&destExtId=" + toId +
                            "&date=" + time.format(DATE_FORMAT) +
                            "&time=" + time.format(TIME_FORMAT)
            );
            xml = get(url);
        } catch (IOException e) {
            return new JourneyList(Collections.emptyList(), "", "");
        }


        return parser.parseTripSearchXml(xml);


    }

    /**
     * return list of locations matching given search term
     */
    public List<Location> getLocationsBySearchTerm(String searchTerm) {
        String xml = "";
        try {
            xml = get(new URL(locationPath + "&input=" + searchTerm));
        } catch (IOException e) {
            Log.e("ApiConnector::getLocationsBySearchTerm", String.format("Route received 404: %s"));
        }

        return parser.parseLocationSearchXml(xml);
    }

    /**
     * return answer of GET request to given url as string
     */
    private String get(URL url) throws IOException {
        StringBuilder result = new StringBuilder();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        return result.toString();

    }

    /**
     * return locations by a gps coordinate
     */
    public List<Location> getLocationsByLatLon(double lat, double lon) {
        try {
            URL url = new URL(
                    gpsPath +
                            "&originCoordLat=" + lat +
                            "&originCoordLong=" + lon
            );

            String xml = get(url);

            return parser.parseGpsSearchXml(xml);
        } catch (IOException e) {
            Log.e("ApiConnector", "Fetching data failed!");
            return Collections.emptyList();
        }
    }

    public JourneyList getMore(Location from, Location to, String scrollForwardData, String scrollBackwardsData, LocalDateTime time) {
        String xml;
        String fromId = from.getApiId();
        String toId = to.getApiId();

        try {
            URL url = new URL(
                    journeyPath +
                            "&originExtId=" + fromId +
                            "&destExtId=" + toId +
                            "&date=" + time.format(DATE_FORMAT) +
                            "&time=" + time.format(TIME_FORMAT) +
                            "&context=" + (scrollForwardData != null ? scrollForwardData : scrollBackwardsData)
            );
            xml = get(url);
        } catch (IOException e) {
            return new JourneyList(Collections.emptyList(), "", "");
        }

        return parser.parseTripSearchXml(xml);

    }

    public Journey refreshJourney(Journey journey) {
        String checksum = journey.getChecksum();
        Location from = journey.getSrcLocation();
        Location to = journey.getTargetLocation();
        LocalDateTime time = journey.getConnections().get(0).getStartTimeObject();

        JourneyList journeys = getDepartures(from, to, time);
        return findJourney(journeys.getJourneys(), checksum);
    }

    private Journey findJourney(List<Journey> journeys, String checksum) {
        return journeys
                .stream()
                .filter(journey -> journey.getChecksum().equals(checksum))
                .findAny()
                .orElse(null);
    }

    private static ApiConnector apiConnector;

    public static void initializeInstance(Resources resources) {
        apiConnector = new ApiConnector(resources);
    }

    public static ApiConnector getInstance() {
        if (apiConnector == null) throw new RuntimeException("ApiConnector instance must be initialized first!");

        return apiConnector;
    }
}