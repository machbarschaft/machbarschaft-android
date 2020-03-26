package com.ks.einanrufhilft.database.entitie;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Class which determines Account Information's
 */
public class Account extends Collection {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private float radius;
    private int credits;

    public Account() {
    }

    public Account(String firstName, String lastName, String phoneNumber, int credits) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.credits = credits;
        this.phoneNumber = phoneNumber;
    }

    public Account(DocumentSnapshot document) {
        this.id = document.getId();
        this.firstName = (String) document.get("first_name");
        this.lastName = (String) document.get("last_name");
        this.phoneNumber = (String) document.get("phone_number");
        if (document.get("radius") != null)
            this.radius = (float) document.get("radius");
        if (document.get("credits") != null)
            this.credits = (int) document.get("credits");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
