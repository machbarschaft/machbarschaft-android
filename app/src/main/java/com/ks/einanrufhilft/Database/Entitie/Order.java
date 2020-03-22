package com.ks.einanrufhilft.Database.Entitie;

/**
 *  Represents an Order. An order is a help request by a help seeking person.
 */
public class Order {

    private String id;
    private String phone_number;
    private String zip;
    private String street;
    private String house_number;
    private String name;
    //private String[] category;
    private String status;
    private String prescription;
    private String carNecessary;
    private String urgency;
    private double lat;
    private double lng;

    public Order() {
    }

    public Order(String id, String phone_number, String plz, String street, String house_number,
                 String name, String[] category, String urgency, String status) {
        this.id = id;
        this.phone_number = phone_number;
        this.zip = plz;
        this.street = street;
        this.house_number = house_number;
        this.name = name;
        //this.category = category;
        this.urgency = urgency;
        this.status = status;
    }

    public Order(String id,String phone_number, String plz, String street, String house_number,
                 String name,  String prescription, String urgency, String status) {
        this.id = id;
        this.phone_number = phone_number;
        this.zip = plz;
        this.street = street;
        this.house_number = house_number;
        this.name = name;
        //this.category = category;
        this.prescription = prescription;
        this.urgency = urgency;
        this.status = status;

    }

    public Order(String id,String phone_number, String plz, String street, String house_number,
                 String name,  String prescription, String urgency, String status, Double lat, Double lng) {
        this.id = id;
        this.phone_number = phone_number;
        this.zip = plz;
        this.street = street;
        this.house_number = house_number;
        this.name = name;
        //this.category = category;
        this.prescription = prescription;
        this.urgency = urgency;
        this.status = status;
        this.lat = lat;
        this.lng = lng;
    }


    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }
/*
    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;

    }
*/

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarNecessary() {
        return carNecessary;
    }

    public void setCarNecessary(String carNecessary) {
        this.carNecessary = carNecessary;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", zip='" + zip + '\'' +
                ", street='" + street + '\'' +
                ", house_number='" + house_number + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", prescription='" + prescription + '\'' +
                ", carNecessary='" + carNecessary + '\'' +
                ", urgency='" + urgency + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
