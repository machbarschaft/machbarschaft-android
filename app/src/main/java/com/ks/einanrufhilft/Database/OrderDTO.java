package com.ks.einanrufhilft.Database;

public class OrderDTO {
    private String phone_number;
    private String adress;
    private String first_name;
    private String last_name;
    private String category;
    private String einkaufszettel;
    private String id;
    private double latitude;
    private double longitude;

    public OrderDTO(String phone_number, String adress, String first_name, String last_name, String category, String einkaufszettel, String id){
        this.phone_number = phone_number;
        this.adress = adress;
        this.first_name = first_name;
        this.last_name = last_name;
        this.category = category;
        this.einkaufszettel = einkaufszettel;
        this.id = id;
    }

    public OrderDTO(){
        this.phone_number = "123";
        this.adress = "Adresse";
        this.first_name = "Muster";
        this.last_name ="Name";
        this.category = "Einkauf";
        this.einkaufszettel = "Nudeln, Soße, Käse";
        this.id = "1";
        this.latitude = 51.0;
        this.longitude = 10.0;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEinkaufszettel() {
        return einkaufszettel;
    }

    public void setEinkaufszettel(String einkaufszettel) {
        this.einkaufszettel = einkaufszettel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "phone_number='" + phone_number + '\'' +
                ", adress='" + adress + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", category='" + category + '\'' +
                ", einkaufszettel='" + einkaufszettel + '\'' +
                ", id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
