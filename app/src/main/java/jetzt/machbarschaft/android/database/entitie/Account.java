package jetzt.machbarschaft.android.database.entitie;

import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Class which determines Account Information's
 */
@EqualsAndHashCode(callSuper = true)
@Data
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


    @NotNull
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
