package jetzt.machbarschaft.android.database;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jetzt.machbarschaft.android.database.callback.CollectionLoadedCallback;
import jetzt.machbarschaft.android.database.callback.CollectionsLoadedCallback;
import jetzt.machbarschaft.android.database.callback.WasSuccessfullCallback;
import jetzt.machbarschaft.android.database.collections.Account;
import jetzt.machbarschaft.android.database.entitie.Address;
import jetzt.machbarschaft.android.database.entitie.Collection;
import jetzt.machbarschaft.android.database.collections.Order;
import jetzt.machbarschaft.android.database.collections.OrderAccount;
import jetzt.machbarschaft.android.database.collections.OrderNew;
import jetzt.machbarschaft.android.database.entitie.Status;
import jetzt.machbarschaft.android.database.entitie.Type;
import jetzt.machbarschaft.android.database.entitie.Urgency;

/**
 * Handles the access to with the database.
 */
public class DataAccess extends Database {
    private static DataAccess dataAccess;

    private DataAccess() {
    }

    public static DataAccess getInstance() {
        if (dataAccess == null) {
            dataAccess = new DataAccess();
        }
        return dataAccess;
    }

    public void createAccount(Account account) {
        createAccount(account, null);
    }

    public void createAccount(Account account, WasSuccessfullCallback callback) {
        super.addDocument(CollectionName.Account, account, callback);
    }

    public void existUser(String phoneNumber, WasSuccessfullCallback callback) {
        getDb().collection(CollectionName.Account.toString()).whereEqualTo("phone_number", phoneNumber).get().addOnCompleteListener(
                task -> {
                    if (task.getResult() == null) {
                        callback.wasSuccessful(false);
                    }
                    callback.wasSuccessful(!task.getResult().isEmpty());
                }
        );
    }

    public void getMyOrders(CollectionsLoadedCallback callback) {
        getMyOrders(Storage.getInstance().getUserID(), callback);
    }

    public void getMyOrders(String userID, CollectionsLoadedCallback callback) {
        LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
        conditions.put("account_id", userID);
        conditions.put("status", "confirmed");
        super.getDocumentsByTwoConditions(CollectionName.Order_Account, conditions,
                documentList -> {
                    ArrayList<Collection> orders = new ArrayList<>();
                    for (DocumentSnapshot d : documentList) {
                        if (d.get("order_id") != null) {
                            orders.add(new Order(d));
                        }
                    }
                    if (!orders.isEmpty()) {
                        callback.onOrdersLoaded(orders);
                    } else {
                        callback.onOrdersLoaded(null);

                    }
                });
    }

    public void getOrderById(String orderId, CollectionLoadedCallback callback) {

        super.getDocumentById(CollectionName.Order, orderId, document -> {
            if (document != null) {
                Order order = new Order(document);
                callback.onOrderLoaded(order);
            } else {
                Log.w("DataAccess", "getOrderById() : document was not found");
                callback.onOrderLoaded(null);
            }
        });
    }

    public void getOrders() {
        getOrders(null);
    }

    public void getOrders(WasSuccessfullCallback callback) {
        super.getCollection(CollectionName.Order, documents -> {
            OrderHandler.getInstance().addCollection(documents);
            //TODO:
            OrderHandler.getInstance().setUserPosition(OrderHandler.Type.Besteller, 50.555809, 9.680845);
            if (callback != null) {
                if (documents != null) {
                    callback.wasSuccessful(true);
                } else {
                    callback.wasSuccessful(false);
                }
            }
        });
    }

    public void setOrderStatus(String orderId, Order.Status status) {
        if (status == Order.Status.CONFIRMED || status == Order.Status.OPEN) {
            // update Status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()));

            //  Adds Entry in Account order
            OrderAccount orderAccount = new OrderAccount();
            orderAccount.setStatus(status.getName());
            orderAccount.setAccountId(Storage.getInstance().getUserID());
            orderAccount.setOrderId(orderId);
            super.addDocument(CollectionName.Order_Account, orderAccount);
        } else if (status == Order.Status.CLOSED) {
            // update status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()));

            // update Status in Order_Account
            super.updateDocument(CollectionName.Order_Account, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()));
        }
    }

    public void setOrderStatus(String orderId, Order.Status status, WasSuccessfullCallback callback) {
        if (status == Order.Status.CONFIRMED) {
            // update Status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()), successful -> {
                if (successful) {
                    //  Adds Entry in Account order
                    OrderAccount orderAccount = new OrderAccount();
                    orderAccount.setStatus(status.getName());
                    orderAccount.setAccountId(Storage.getInstance().getUserID());
                    orderAccount.setOrderId(orderId);
                    super.addDocument(CollectionName.Order_Account, orderAccount, innerSuccessful -> {
                        if (innerSuccessful) {
                            callback.wasSuccessful(true);
                        } else {
                            callback.wasSuccessful(false);
                        }
                    });
                } else {
                    callback.wasSuccessful(false);
                }
            });
        } else if (status == Order.Status.CLOSED) {
            // update status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()), successful -> {
                if (successful) {

                    super.getOneDocumentByCondition(CollectionName.Order_Account, new AbstractMap.SimpleEntry<>("order_id", orderId),
                            document -> {
                                document.getId();
                                // update Status in Order_Account
                                super.updateDocument(CollectionName.Order_Account, document.getId(), new AbstractMap.SimpleEntry<>("status", status.getName()), innerSuccessful -> {
                                    if (innerSuccessful) {
                                        callback.wasSuccessful(true);
                                    } else {
                                        callback.wasSuccessful(false);
                                    }
                                });
                            });
                } else {
                    callback.wasSuccessful(false);
                }
            });


        }
    }

    public void createOrder(Context context, Address address, boolean car_necessary, boolean prescription, String name, Type type, Urgency urgency, WasSuccessfullCallback callback) {

        // Step 1 get The GeoPoint
        this.geoRequest(context, address);

        // Create Order Object and store it
        OrderNew newOrder = new OrderNew();
        newOrder.setAddress(address.toHashMap());
        newOrder.setExtras(new HashMap<String, Boolean>() {{
            put("car_necessary", car_necessary);
            put("prescription", prescription);
        }});

        android.location.Address geoAddress = this.geoRequest(context, address);

        if (geoAddress != null) {

            // GeoHash hash = GeoHash.fromLocation(location, 9);
            HashMap<String, ?> location = new HashMap<String, Object>() {{
                //put("geohash", );
                put("gps", new GeoPoint(geoAddress.getLatitude(), geoAddress.getLongitude()));
            }};

            newOrder.setName(name);
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String phoneNumber = firebaseUser.getPhoneNumber();
            newOrder.setPhone_number(phoneNumber);
            newOrder.setPrivacy_agreed(true);
            newOrder.setStatus(Status.OPEN);
            newOrder.setType(type);
            newOrder.setUrgency(urgency);

        super.addDocument(CollectionName.Order, newOrder, callback);
        }
    }


    private android.location.Address geoRequest(Context context, Address address) {
        Geocoder geocoder = new Geocoder(context);
        // Format: String locationName, int maxResults, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude
        String stringAddress = address.getHouse_number() + ", " + address.getStreet() + ", " + address.getCity() + ", " + address.getZip();

        List<android.location.Address> geoAddress = null;
        try {
            geoAddress = geocoder.getFromLocationName(stringAddress, 2);
            return geoAddress.get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
