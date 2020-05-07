package jetzt.machbarschaft.android.database.collections;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

import jetzt.machbarschaft.android.database.entitie.Collection;
import jetzt.machbarschaft.android.database.entitie.Status;
import jetzt.machbarschaft.android.database.entitie.Type;
import jetzt.machbarschaft.android.database.entitie.Urgency;

public class OrderNew extends Collection {
    private String account_id;
    private HashMap<String, ?> address;
    private HashMap<String, Boolean> extras;
    private GeoPoint location;
    private String name;
    private String phone_number;
    private boolean privacy_agreed;
    private Status status;
    private Type type;
    private Urgency urgency;

    public OrderNew() {}

    public OrderNew(String account_id, HashMap<String, ?> address, HashMap<String, Boolean> extras, GeoPoint location, String name, String phone_number, boolean privacy_agreed, Status status, Type type, Urgency urgency) {
        this.account_id = account_id;
        this.address = address;
        this.extras = extras;
        this.location = location;
        this.name = name;
        this.phone_number = phone_number;
        this.privacy_agreed = privacy_agreed;
        this.status = status;
        this.type = type;
        this.urgency = urgency;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public HashMap<String, ?> getAddress() {
        return address;
    }

    public void setAddress(HashMap<String, ?> address) {
        this.address = address;
    }

    public HashMap<String, Boolean> getExtras() {
        return extras;
    }

    public void setExtras(HashMap<String, Boolean> extras) {
        this.extras = extras;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isPrivacy_agreed() {
        return privacy_agreed;
    }

    public void setPrivacy_agreed(boolean privacy_agreed) {
        this.privacy_agreed = privacy_agreed;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }
}
