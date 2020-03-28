package jetzt.machbarschaft.android.database.entitie;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import jetzt.machbarschaft.android.R;

/**
 * Represents an Order. An order is a help request by a help seeking person.
 */
public class Order extends Collection {
    /**
     * The id of the entry in the data base. This id uniquely identifies this order.
     */
    private String id;
    /**
     * The id of the list
     */
    private int listId;
    @NotNull
    private Type type;
    private Status status;
    @NotNull
    private Urgency urgency;
    private String clientName;
    private String phoneNumber;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String city;
    private boolean getPrescription;
    private boolean carNecessary;
    private double latitude;
    private double longitude;

    public Order() {
        type = Type.OTHER;
        urgency = Urgency.UNDEFINED;
    }

    public Order(String id, @NotNull Type type, String clientName, String phoneNumber, String street,
                 String houseNumber, String zipCode, String city, Status status, @NotNull Urgency urgency,
                 boolean getPrescription, boolean carNecessary, double latitude, double longitude) {
        this.id = id;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.clientName = clientName;
        this.status = status;
        this.getPrescription = getPrescription;
        this.carNecessary = carNecessary;
        this.urgency = urgency;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Order(DocumentSnapshot document) {
        id = document.getId();
        // TODO get type form document
        type = Type.byName(null);
        status = Status.byName(document.getString("status"));
        urgency = Urgency.byName(document.getString("urgency"));
        clientName = document.getString("name");
        phoneNumber = document.getString("phone_number");
        street = document.getString("street");
        houseNumber = document.getString("house_number");
        zipCode = document.getString("zip");
        city = document.getString("city");
        getPrescription = "yes".equals(document.getString("prescription"));
        carNecessary = "yes".equals(document.getString("carNecessary"));
        if (document.get("lat") != null && document.get("lng") != null) {
            latitude = document.getDouble("lat");
            longitude = document.getDouble("lng");
        }
    }

    /**
     * Gets the unique id of the order in the database.
     *
     * @return The unique id of the order.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique id of the order.
     *
     * @param id The new id of the order.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the id of the order in the displayed list. This id is used to allow the user to match
     * the flags in the list with the flags on the map.
     * <p>
     * This id is used only locally, not in the database. It will be set by the activity displaying
     * the list.
     *
     * @return The number of the order in the list.
     */
    public int getListId() {
        return listId;
    }

    /**
     * Sets the id of the order in the displayed list. This id is used to allow the user to match
     * the flags in the list with the flags on the map.
     * <p>
     * This id is used only locally, not in the database. It will be set by the activity displaying
     * the list.
     *
     * @param listId The new number of the order in the list.
     */
    public void setListId(int listId) {
        this.listId = listId;
    }

    /**
     * Gets the type of the order. This defines whether the order is for normal groceries, or e.g.
     * medicine.
     *
     * @return The type of the order.
     */
    @NotNull
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the order.
     *
     * @param type The new type of the order.
     */
    public void setType(@NotNull Type type) {
        this.type = type;
    }

    /**
     * Gets the status of the order that determines, whether it can still be processed.
     *
     * @return The status of the order.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the order.
     *
     * @param status The new status of the order.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Gets the urgency of the order that determines the time that is available until the order
     * should be processed.
     *
     * @return The urgency of the order.
     */
    @NotNull
    public Urgency getUrgency() {
        return urgency;
    }

    /**
     * Sets the urgency of the order.
     *
     * @param urgency The new urgency of the order.
     */
    public void setUrgency(@NotNull Urgency urgency) {
        this.urgency = urgency;
    }

    /**
     * Gets the name of the client that issued the order.
     *
     * @return The name of the client.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Sets the name of the client.
     *
     * @param clientName The new name of the client.
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Gets the phone number of the client that issued the order.
     *
     * @return The phone number of the client.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the client that issued the order.
     *
     * @param phoneNumber The new phone number of the client.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the street that the client lives in.
     *
     * @return The street of the address of the client.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street that the client lives in.
     *
     * @param street The new street of the client.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Gets the house number of the address of the client.
     *
     * @return The house number of the client.
     */
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * Sets the house number of the client.
     *
     * @param houseNumber The new house number of the address of the client.
     */
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    /**
     * Gets the zip code of address of the client.
     *
     * @return The zip code of the client.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zip code of the address of the client.
     *
     * @param zipCode The new zip code of the client.
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Gets the city that the client lives in.
     *
     * @return The city of the client.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city that the client lives in.
     *
     * @param city The new city of the client.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets whether a user has to get the prescription before processing the order.
     *
     * @return Whether a user has to get the prescription first.
     */
    public boolean getPrescription() {
        return getPrescription;
    }

    /**
     * Sets whether a user has to get the prescription before processing the order.
     *
     * @param getPrescription Whether a user has to get the prescription first.
     */
    public void setGetPrescription(boolean getPrescription) {
        this.getPrescription = getPrescription;
    }

    /**
     * Gets whether a car is necessary, or at least recommended to process the order.
     *
     * @return Whether a car is necessary for the order.
     */
    public boolean isCarNecessary() {
        return carNecessary;
    }

    /**
     * Sets whether a car is necessary, or at least recommended to process the order.
     *
     * @param carNecessary Whether a car is necessary for the order.
     */
    public void setCarNecessary(boolean carNecessary) {
        this.carNecessary = carNecessary;
    }

    /**
     * Gets the latitude of the clients location.
     *
     * @return The latitude of the location.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the clients location.
     *
     * @param latitude The new latitude of the location.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the clients location.
     *
     * @return The longitude of the location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the clients location.
     *
     * @param longitude The new longitude of the location.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the complete address of the client.
     *
     * @return The address of the client.
     */
    public String getCompleteAddress() {
        return street + " " + houseNumber + ", " + zipCode + " " + city;
    }

    /**
     * Gets the location of the client.
     *
     * @return The location of the client.
     */
    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }

    @NotNull
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
                ", zip='" + zipCode + '\'' +
                ", street='" + street + '\'' +
                ", house_number='" + houseNumber + '\'' +
                ", name='" + clientName + '\'' +
                ", status='" + status + '\'' +
                ", prescription='" + getPrescription + '\'' +
                ", carNecessary='" + carNecessary + '\'' +
                ", urgency='" + urgency + '\'' +
                ", lat=" + latitude +
                ", lng=" + longitude +
                '}';
    }

    /**
     * The type the order.
     */
    public enum Type {
        GROCERIES("EINKAUFEN", R.string.order_title_groceries),
        MEDICINE("APOTHEKE", R.string.order_title_medicine),
        OTHER("SONSTIGES", R.string.order_title_other);

        @NotNull
        private final String name;
        @StringRes
        private final int title;

        Type(@NotNull String name, @StringRes int title) {
            this.name = name;
            this.title = title;
        }

        /**
         * Gets the appropriate type for the given internal name.
         *
         * @param name The internal name of the type.
         * @return The parsed type.
         */
        @NotNull
        public static Type byName(String name) {
            for (Type type : values()) {
                if (type.name.equals(name)) {
                    return type;
                }
            }
            return OTHER;
        }

        /**
         * Gets the internal name of the type. This is the name used in firebase.
         *
         * @return The internal name of the type.
         */
        @NotNull
        public String getName() {
            return name;
        }

        /**
         * Gets the string resource of the tile of the type.
         *
         * @return The resource id of the type title.
         */
        @StringRes
        public int getTitle() {
            return title;
        }
    }

    /**
     * The status of an order defines whether it has been processed, is in process or can be
     * processed by some one.
     */
    public enum Status {
        OPEN("open"),
        CONFIRMED("confirmed"),
        CLOSED("closed");

        @NotNull
        private final String name;

        Status(@NotNull String name) {
            this.name = name;
        }

        /**
         * Gets the appropriate status for the given internal name.
         *
         * @param name The internal name of the status.
         * @return The parsed status.
         */
        @NotNull
        public static Status byName(String name) {
            for (Status status : values()) {
                if (status.name.equals(name)) {
                    return status;
                }
            }
            return OPEN;
        }

        /**
         * Gets the internal name of the status. This is the name used in firebase.
         *
         * @return The internal name of the status.
         */
        @NotNull
        public String getName() {
            return name;
        }
    }

    /**
     * The urgency defines how fast the order should be processed.
     */
    public enum Urgency {
        URGENT("ASAP", R.drawable.ic_order_urgent, R.string.order_urgency_urgent),
        TODAY("TODAY", R.drawable.ic_order_today, R.string.order_urgency_today),
        TOMORROW("TOMORROW", R.drawable.ic_order_tomorrow, R.string.order_urgency_tomorrow),
        UNDEFINED("UNDEFINED", R.drawable.ic_order_undefined, R.string.order_urgency_undefined);

        @NotNull
        private final String name;
        @DrawableRes
        private final int icon;
        @StringRes
        private final int title;

        Urgency(@NotNull String name, @DrawableRes int icon, @StringRes int title) {
            this.name = name;
            this.icon = icon;
            this.title = title;
        }

        /**
         * Gets the appropriate urgency for the given internal name.
         *
         * @param name The internal name of the urgency.
         * @return The parsed urgency.
         */
        @NotNull
        public static Urgency byName(String name) {
            for (Urgency urgency : values()) {
                if (urgency.name.equals(name)) {
                    return urgency;
                }
            }
            return UNDEFINED;
        }

        /**
         * Gets the internal name of the urgency. This is the name used in firebase.
         *
         * @return The internal name of the urgency.
         */
        @NotNull
        public String getName() {
            return name;
        }

        /**
         * Gets the icon for a marker of this urgency.
         *
         * @return The resource id of the icon to use as a marker.
         */
        @DrawableRes
        public int getIconRes() {
            return icon;
        }

        /**
         * Gets the display title of the urgency.
         *
         * @return The resource id of the title.
         */
        @StringRes
        public int getTitle() {
            return title;
        }
    }
}
