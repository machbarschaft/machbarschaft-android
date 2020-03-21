package com.ks.einanrufhilft.Database.Entitie;

public class Order {

    private String id;
    private String phone_number;
    private String zip;
    private String street;
    private String house_number;
    private String first_name;
    private String last_name;
    private String[] category;
    private String einkaufsZettel;


    public Order() {
    }

    public Order(String id, String phone_number, String plz, String street, String house_number,
                 String firstName, String lastNamme, String[] category) {
        this.id = id;
        this.phone_number = phone_number;
        this.zip = plz;
        this.street = street;
        this.house_number = house_number;
        this.first_name = firstName;
        this.last_name = lastNamme;
        this.category = category;
    }

    public Order(String id,String phone_number, String plz, String street, String house_number,
                 String firstName, String lastNamme, String[] category, String einkaufsliste) {
        this.id = id;
        this.phone_number = phone_number;
        this.zip = plz;
        this.street = street;
        this.house_number = house_number;
        this.first_name = firstName;
        this.last_name = lastNamme;
        this.category = category;
        this.einkaufsZettel = einkaufsliste;
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

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;

    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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

    public String getEinkaufsZettel() {
        return einkaufsZettel;
    }

    public void setEinkaufsZettel(String einkaufsZettel) {
        this.einkaufsZettel = einkaufsZettel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
