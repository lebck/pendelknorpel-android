package de.hsrm.lback.myapplication.network;

import android.util.Log;

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
    private static final String API_KEY = "1568d5db0a471d5e1d5ec7017f79ca38";
    private static final String BASE_URL = "/fahrplan-plus/v1";
    private static final String DOMAIN = "https://api.deutschebahn.com";
    private static final String DEPARTUES_URL = "/departureBoard";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Journey> getDepartures(Location from, Location to, LocalDateTime time) throws IOException {
        int toId = to.getApiId();
        int fromId = from.getApiId();

        // List<Journey> journeys = JSONParser.parse(result)

        return getTestData(from, to);


    }

    private String getDepartures(int fromId, LocalDateTime time) throws IOException {
        URL url = new URL(
                DOMAIN +
                        BASE_URL +
                        DEPARTUES_URL +
                        "/" +
                        fromId +
                        "?date=" +
                        time.format(FORMAT)
        );

        return get(url);

    }

    private String getArrivals(int toId, LocalDateTime time) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private String get(URL url) throws IOException {
        StringBuilder result = new StringBuilder();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
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
            journeys.add(new Journey(src, target, connections));
        }

        return journeys;
    }

}