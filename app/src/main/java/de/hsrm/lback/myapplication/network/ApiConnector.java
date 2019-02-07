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
import java.util.List;
import java.util.Random;

import de.hsrm.lback.myapplication.models.Connection;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.Vehicle;

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
            e.printStackTrace();
        }


        return parser.parseTripSearchXml(xml);


    }


    private String getArrivals(int toId, LocalDateTime time) {
        throw new UnsupportedOperationException("Not implemented");
    }

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

    private List<Journey> getTestData(Location src, Location target) {
        // TODO make network request to DB API
        int m = 100;
        List<Journey> journeys = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < m; i++) {

            List <Connection> connections = Arrays.asList(
                    new Connection(
                            src,
                            new Location("zwischenstopp", 0),
                            LocalDateTime.now().plusHours(i),
                            LocalDateTime.now().plusHours(1 + i),
                            Integer.toString(r.nextInt(10)),
                            Vehicle.BUS),
                    new Connection(
                            new Location("zwischenstopp", 0),
                            target,
                            LocalDateTime.now().plusHours(1 + i),
                            LocalDateTime.now().plusHours(2 + i),
                            Integer.toString(r.nextInt(10)),
                            Vehicle.BUS)
            );
            journeys.add(new Journey(connections));
        }

        return journeys;
    }

}