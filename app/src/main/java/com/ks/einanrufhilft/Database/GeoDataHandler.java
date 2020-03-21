package com.ks.einanrufhilft.Database;


import java.util.ArrayList;

public class GeoDataHandler {

    private static GeoDataHandler geoDataHandler;
    private GeoDataPerson lieferant;
    private ArrayList<GeoDataPerson> persons;

    private double closeDistanceSetting; // radius in dem Personen angezeigt werden sollen

    private GeoDataHandler() {
        this.persons = new ArrayList<>();
    }

    public static GeoDataHandler getInstance() {
        if (geoDataHandler == null) {
            geoDataHandler = new GeoDataHandler();
        }
        return geoDataHandler;
    }


    public void add(Type type, double lat, double lng) {
        if(type == Type.Besteller) {
            this.persons.add(new GeoDataPerson(type, lat, lng));
        } else{
            this.lieferant = new GeoDataPerson(type, lat, lng);
        }
    }

    public ArrayList<GeoDataPerson> getPersonInDistance() {
        return getPersonInDistance(this.closeDistanceSetting);
    }

    public ArrayList<GeoDataPerson> getPersonInDistance(double kmDistance) {
        ArrayList<GeoDataPerson> closePersons = new ArrayList<>();
        for(GeoDataPerson person: persons) {
            if(this.getDistance(person) < this.closeDistanceSetting) {
                closePersons.add(person);
            }
        }
        return closePersons;
    }

    private double getDistance(GeoDataPerson geoData) {

    double lat1 = geoData.lat;
    double lon1 = geoData.lng;
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



    enum Type {
        Lieferant, // Lieferant
        Besteller   // Besteller
    }

    private class GeoDataPerson {
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

}
