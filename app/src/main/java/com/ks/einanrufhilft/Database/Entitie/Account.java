package com.ks.einanrufhilft.Database.Entitie;

/**
 * Class which determines Account Information's
 */
public class Account {
    private String id;
    private String first_name;
    private String last_name;
    private String phone_number;
    private float radius;
    private int credits;

    public Account() {}

    public Account(String first_name, String last_name, String phone_number, float radius, int credits) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.credits = credits;
        this.phone_number = phone_number;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
}
