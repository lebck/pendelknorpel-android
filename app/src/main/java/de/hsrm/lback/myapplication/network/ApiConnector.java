package de.hsrm.lback.myapplication.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.hsrm.lback.myapplication.models.Connection;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.Vehicle;

/**
 * Controls connection to apis (currently only RMV API)
 */
public class ApiConnector {
    private static final String API_KEY = "824c7332-4e5c-4f3e-84f7-5f925c2e3dd7";
    private static final String BASE_URL = "/hapi";
    private static final String DOMAIN = "https://www.rmv.de";
    private static final String AUTH_PATH = String.format("?accessId=%s", API_KEY);
    private static final String LOCATION_PATH =
            DOMAIN + BASE_URL + "/location.name" + AUTH_PATH;
    private static final String JOURNEY_PATH =
            DOMAIN + BASE_URL + "/trip" + AUTH_PATH;

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private XMLParser parser;

    public ApiConnector() {
        parser = new XMLParser();
    }

    /**
     * return List of journeys that match the start and end location and the time
     */
    public List<Journey> getDepartures(Location from, Location to, LocalDateTime time) {
        String fromId = from.getApiId();
        String toId = to.getApiId();
        String xml = "";
        try {
            URL url = new URL(
                    JOURNEY_PATH +
                            "&originExtId=" + fromId +
                            "&destExtId=" + toId +
                            "&date=" + time.format(FORMAT)
            );
            xml = get(url);
        } catch (IOException e) {
            return Collections.emptyList();
        }


        return parser.parseTripSearchXml(xml);


    }

    /**
     * return list of locations matching given search term
     */
    public List<Location> getLocationsBySearchTerm(String searchTerm) {
        String xml = "";
        try {
            xml = get(new URL(LOCATION_PATH + "&input=" + searchTerm));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
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
}