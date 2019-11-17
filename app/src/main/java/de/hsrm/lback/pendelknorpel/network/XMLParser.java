package de.hsrm.lback.pendelknorpel.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.hsrm.lback.pendelknorpel.domains.journey.models.Connection;
import de.hsrm.lback.pendelknorpel.domains.journey.models.Journey;
import de.hsrm.lback.pendelknorpel.domains.journey.models.JourneyList;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;

/**
 * Parse xml by rmv api
 */
public class XMLParser {
    private static final String LOCATION_TAG_NAME = "StopLocation";
    private static final String JOURNEY_TAG_NAME = "Trip";
    private static final String LEG_TAG_NAME = "Leg";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DocumentBuilder builder;

    private class ElementsResult {
        public NodeList elements;
        public Element root;
    }

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

    /**
     * return all elements matching tagName from given xml string
     */
    private ElementsResult getElements(String xml, String tagName) throws IOException, SAXException {
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = doc.getDocumentElement();

        ElementsResult elementsResult = new ElementsResult();
        elementsResult.elements = root.getElementsByTagName(tagName);
        elementsResult.root = root;

        return elementsResult;

    }

    /**
     * Parse Element and return Location
     */
    private Location getLocationByLocationElement(Element element) {

        String name = element.getAttribute("name");
        String apiId = element.getAttribute("extId");

        Location l = new Location();

        l.setApiId(apiId);

        l.setName(name);

        return l;

    }

    /**
     * Parse NodeList of StopLocation Elements
     * @param stopLocationNodeList
     * @return
     */
    private List<Location> getLocationsByStopLocationNodeList(NodeList stopLocationNodeList) {

        List<Location> locations = new ArrayList<>();

        for (int i = 0; i < stopLocationNodeList.getLength(); i++) {
            locations.add(getLocationByLocationElement((Element) stopLocationNodeList.item(i)));
        }

        return locations;

    }

    /** create TimeList containing planned and real times for a connection */
    private TimeList getTimesByLegElement(Element originElement, Element destinationElement) {
        TimeList list = new TimeList();

        // create planned times:
        String startDate = originElement.getAttribute("date");
        String startTime = originElement.getAttribute("time");

        String endDate = destinationElement.getAttribute("date");
        String endTime = destinationElement.getAttribute("time");

        list.startDateTime = LocalDateTime.parse(startDate + " " + startTime, FORMAT);
        list.endDateTime = LocalDateTime.parse(endDate + " " + endTime, FORMAT);

        // create real times, if they exist:
        String realStartTime = originElement.getAttribute("rtTime");
        String realStartDate = originElement.getAttribute("rtDate");

        String realEndTime = destinationElement.getAttribute("rtTime");
        String realEndDate = destinationElement.getAttribute("rtDate");

        try {
            list.realStartDateTime =
                    LocalDateTime.parse(realStartDate + " " + realStartTime, FORMAT);
            list.realEndDateTime =
                    LocalDateTime.parse(realEndDate + " " + realEndTime, FORMAT);
        } catch (DateTimeParseException e) {
            list.realStartDateTime = list.startDateTime;
            list.realEndDateTime = list.endDateTime;
        }

        return list;
    }

    /**
     * Parse Leg XML Element and return connection
     */
    private Connection getConnectionByLegElement(Element leg) {
        // Extract values from given element
        Element originElement = (Element) leg.getElementsByTagName("Origin").item(0);
        Element destinationElement = (Element) leg.getElementsByTagName("Destination").item(0);
        String vehicleString = leg.getAttribute("name");

        Location from = getLocationByLocationElement(originElement);
        Location to = getLocationByLocationElement(destinationElement);

        TimeList list = getTimesByLegElement(originElement, destinationElement);

        // insert values in new connection
        Connection connection = new Connection();

        connection.setStartLocation(from);
        connection.setEndLocation(to);
        connection.setStartTimeObject(list.startDateTime);
        connection.setEndTimeObject(list.endDateTime);
        connection.setRealEndTimeObject(list.realEndDateTime);
        connection.setRealStartTimeObject(list.realStartDateTime);
        connection.setLineId(vehicleString);
        connection.setVehicle("BUS");

        return connection;
    }

    /**
     * Parse Trip XML Element to list of connections
     */
    private List<Connection> getConnectionsByTripElement(Element tripElement) {
        List<Connection> connections = new ArrayList<>();

        NodeList legs = tripElement.getElementsByTagName(LEG_TAG_NAME);

        for (int i = 0; i < legs.getLength(); i++) {
            Element leg = (Element) legs.item(i);

            connections.add(getConnectionByLegElement(leg));


        }

        return connections;


    }

    /**
     * Parse single Trip Element and return a journey
     */
    private Journey getJourneyByTripElement(Node item) {
        Element element = (Element) item;


        Journey journey = new Journey();

        journey.setConnections(getConnectionsByTripElement(element));
        journey.setChecksum(element.getAttribute("checksum"));

        return journey;
    }

    /**
     * Parse list of Trip XML Elements and return a list of journeys
     */
    private JourneyList getJourneysByTripNodeList(NodeList trips, String scrollF, String scrollB) {
        List<Journey> journeys = new ArrayList<>();
        for (int i = 0; i < trips.getLength(); i++) {
            journeys.add(getJourneyByTripElement(trips.item(i)));
        }

        return new JourneyList(journeys, scrollF, scrollB);
    }

    /**
     * Parse XML returned by a request to the search api
     */
    public List<Location> parseLocationSearchXml(String xml) {
        try {
            NodeList nodes = getElements(xml, LOCATION_TAG_NAME).elements;

            return getLocationsByStopLocationNodeList(nodes);

        } catch (IOException | SAXException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }


    }

    /**
     * Parse XML returned by a request to the search api
     */
    public JourneyList parseTripSearchXml(String xml) {
        try {

            ElementsResult result = getElements(xml, JOURNEY_TAG_NAME);
            NodeList trips = result.elements;
            Element root = result.root;

            String scrollF = root.getAttribute("scrF");
            String scrollB = root.getAttribute("scrB");


            return getJourneysByTripNodeList(trips, scrollF, scrollB);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            return new JourneyList(Collections.emptyList(), "", "");
        }
    }

    /**
     * Parse XML returned by a request to the "nearby locations" api
     */
    public List<Location> parseGpsSearchXml(String xml) {

        return parseLocationSearchXml(xml);
    }

    private class TimeList {
        public LocalDateTime startDateTime;
        public LocalDateTime endDateTime;
        public LocalDateTime realStartDateTime;
        public LocalDateTime realEndDateTime;
    }
}
