package jetzt.machbarschaft.android.database.entitie;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

/**
 * Class which determines Account Information's
 */
public class Account extends Collection implements Serializable {
    private String id;
    private String first_name;
    private String last_name;
    private String phone_number;
    private double radius;
    private int credits;

    public Account() {
    }

    public Account(String first_name, String last_name, String phone_number, int credits) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.credits = credits;
        this.phone_number = phone_number;
    }

    public Account(DocumentSnapshot document) {
        this.id = document.getId();
        this.first_name = (String) document.get("first_name");
        this.last_name = (String) document.get("last_name");
        this.phone_number = (String) document.get("phone_number");
        if (document.get("radius") != null)
            this.radius = (double) document.get("radius");
        if (document.get("credits") != null)
            this.credits =  Long.valueOf((long)document.get("credits")).intValue();
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String firstName) {
        this.first_name = firstName;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String lastName) {
        this.last_name = lastName;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
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


    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", radius=" + radius +
                ", credits=" + credits +
                '}';
    }
}
