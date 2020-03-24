package com.ks.einanrufhilft.Database;


import com.google.firebase.firestore.DocumentSnapshot;
import com.ks.einanrufhilft.Database.Entitie.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Orders
 */
public class OrderHandler {

    private static OrderHandler orderHandler;
    private GeoDataPerson lieferant;
    //private ArrayList<GeoDataPerson> persons;
    private ArrayList<Order> orders;


    private double closeDistanceSetting; // radius in dem Personen angezeigt werden sollen

    private OrderHandler() {
        this.orders = new ArrayList<>();
    }

    public static OrderHandler getInstance() {
        if (orderHandler == null) {
            orderHandler = new OrderHandler();
        }
        return orderHandler;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void addOrderList(List<DocumentSnapshot> documents) {

    }
    /*
    public void add(Type type, double lat, double lng) {
        if(type == Type.Besteller) {
            this.persons.add(new GeoDataPerson(type, lat, lng));
        } else{
            this.lieferant = new GeoDataPerson(type, lat, lng);
        }
    }

     */

    /**
     * Gets the distance to the order.
     * @param firstPersonLat person Doing Request
     * @param firstPersonLon person Doing Request
     * @param secondPersonLat Order Data
     * @param secondPersonLon Order Data
     * @return distance in metres
     */
    public static double getDistance(double firstPersonLat, double firstPersonLon, double secondPersonLat, double secondPersonLon) {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(secondPersonLat - firstPersonLat);
        double lonDistance = Math.toRadians(secondPersonLon - firstPersonLon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(firstPersonLat)) * Math.cos(Math.toRadians(secondPersonLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = 0; //Height differences are currently ignored
        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public ArrayList<Order> getPersonInDistance() {
        return getPersonInDistance(this.closeDistanceSetting);
    }

    /**
     *  Gives us the Persons within a specific distance.
     * @param kmDistance in which radius the persons should be
     * @return persons in specific distance
     */
    public ArrayList<Order> getPersonInDistance(double kmDistance) {
        ArrayList<Order> closeOrders = new ArrayList<>();
        for(Order order: orders) {
            if(this.getDistance(order) < kmDistance) {
                closeOrders.add(order);
            }
        }
        return closeOrders;
    }

    private double getDistance(Order order) {

    double lat1 = order.getLatitude();
    double lon1 = order.getLongitude();
    double lat2 = this.lieferant.lat;
    double lon2 = this.lieferant.lng;

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0;  // höhenunterschiede der Personen werden ignoriert

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);

    }

    public void addCollection(List<DocumentSnapshot> documents) {
        // reset orders
        this.orders = null;
        this.orders = new ArrayList<>();

        for(DocumentSnapshot doc: documents) {
            this.orders.add(new Order(doc));
        }
    }


    enum Type {
        Lieferant,
        Besteller
    }

    // wird aktuell nur noch für den app nutzer verwendet:
    public class GeoDataPerson {
        GeoDataPerson(Type type, double lat, double lng) {
            this.type = type;
            this.lat = lat;
            this.lng = lng;
        }
        private Type type;
        private double lat;
        private double lng;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    public double getCloseDistanceSetting() {
        return closeDistanceSetting;
    }

    public void setCloseDistanceSetting(double closeDistanceSetting) {
        this.closeDistanceSetting = closeDistanceSetting;
    }

    public GeoDataPerson getLieferant() {
        return lieferant;
    }

    public void setLieferant(Type type, double lat, double lng) {
        this.lieferant = new GeoDataPerson(type,lat, lng);
    }


    @Override
    public String toString() {
        return "OrderHandler{" +
                "lieferant=" + lieferant +
                ", orders=" + orders +
                ", closeDistanceSetting=" + closeDistanceSetting +
                '}';
    }
}
