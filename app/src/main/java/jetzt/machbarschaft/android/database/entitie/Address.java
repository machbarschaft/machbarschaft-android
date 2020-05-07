package jetzt.machbarschaft.android.database.entitie;

import java.util.HashMap;

public class Address {
    private String city;
    private boolean confirmed;
    private String house_number;
    private String street;
    private String zip;

    public Address(String city, boolean confirmed, String house_number, String street, String zip) {
        this.city = city;
        this.confirmed = confirmed;
        this.house_number = house_number;
        this.street = street;
        this.zip = zip;
    }

    public HashMap<String, ?> toHashMap() {
        return new HashMap<String, Object>(){{
            put("city", city);
            put("confirmed", confirmed);
            put("house_number", house_number);
            put("street", street);
            put("zip", zip);
        }};
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
