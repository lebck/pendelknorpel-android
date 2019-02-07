package de.hsrm.lback.myapplication.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.hsrm.lback.myapplication.models.Connection;
import de.hsrm.lback.myapplication.models.Journey;
import de.hsrm.lback.myapplication.models.Location;

public class XMLParser {
    private static final String LOCATION_TAG_NAME = "StopLocation";
    private static final String JOURNEY_TAG_NAME = "Trip";
    private static final String LEG_TAG_NAME = "Leg";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DocumentBuilder builder;

    public XMLParser() {
        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    private NodeList getElements(String xml, String tagName) throws IOException, SAXException {
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = doc.getDocumentElement();

        return root.getElementsByTagName(tagName);

    }

    private Location getLocationByLocationElement(Element element) {

        String name = element.getAttribute("name");
        String apiId = element.getAttribute("extId");

        Location l = new Location();

        l.setApiId(apiId);

        l.setName(name);

        return l;

    }

    private List<Location> getLocationsByStopLocationNodeList(NodeList stopLocationNodeList) {

        List<Location> locations = new ArrayList<>();

        for (int i = 0; i < stopLocationNodeList.getLength(); i++) {
            locations.add(getLocationByLocationElement((Element) stopLocationNodeList.item(i)));
        }

        return locations;

    }

    private Connection getConnectionByLegElement(Element leg) {
        Element originElement = (Element) leg.getElementsByTagName("Origin").item(0);
        Element destinationElement = (Element) leg.getElementsByTagName("Destination").item(0);
        String vehicleString = leg.getAttribute("name");
        String startDate = originElement.getAttribute("date");
        String startTime = originElement.getAttribute("time");
        String endDate = destinationElement.getAttribute("date");
        String endTime = destinationElement.getAttribute("time");

        LocalDateTime startDateTime = LocalDateTime.parse(startDate + " " + startTime, FORMAT);

        LocalDateTime endDateTime = LocalDateTime.parse(endDate + " " + endTime, FORMAT);

        Location from = getLocationByLocationElement(originElement);
        Location to = getLocationByLocationElement(destinationElement);

        Connection connection = new Connection();

        connection.setStartLocation(from);
        connection.setEndLocation(to);
        connection.setStartTimeObject(startDateTime);
        connection.setEndTimeObject(endDateTime);
        connection.setLineId(vehicleString);
        connection.setVehicle("BUS");

        return connection;
    }

    private List<Connection> getConnectionsByTripElement(Element tripElement) {
        List<Connection> connections = new ArrayList<>();

        NodeList legs = tripElement.getElementsByTagName(LEG_TAG_NAME);

        for (int i = 0; i < legs.getLength(); i++) {
            Element leg = (Element) legs.item(i);

            connections.add(getConnectionByLegElement(leg));


        }

        return connections;


    }

    private Journey getJourneyByTripElement(Node item) {
        Element element = (Element) item;


        Journey journey = new Journey();

        journey.setConnections(getConnectionsByTripElement(element));


        return journey;
    }

    private List<Journey> getJourneysByTripNodeList(NodeList trips) {
        List<Journey> journeys = new ArrayList<>();
        for (int i = 0; i < trips.getLength(); i++) {
            journeys.add(getJourneyByTripElement(trips.item(i)));
        }

        return journeys;
    }



    public List<Location> parseLocationSearchXml(String xml) {
        try {
            NodeList nodes = getElements(xml, LOCATION_TAG_NAME);

            return getLocationsByStopLocationNodeList(nodes);

        } catch (IOException | SAXException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }


    }

    public List<Journey> parseTripSearchXml(String xml) {
        try {
            NodeList trips = getElements(xml, JOURNEY_TAG_NAME);

            return getJourneysByTripNodeList(trips);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
