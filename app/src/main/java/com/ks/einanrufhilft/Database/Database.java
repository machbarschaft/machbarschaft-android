package com.ks.einanrufhilft.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ks.einanrufhilft.Database.Entitie.Account;
import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.Database.Entitie.Order_Account;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * All database accesses are made here.
 */
public class Database {
    private FirebaseFirestore db;
    private static Database myDBClass;

    public static Database getInstance() {
        if (myDBClass == null) {
            myDBClass = new Database();
        }
        return myDBClass;
    }

    private Database() {
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public enum Status            // Enum-Typ
    {
        Open, Confirmed, Closed;  // Enumerationskonstanten
    }

    public void getCloseRequests() {

    }

    /**
     * Creating a new Account in the database.
     *
     * @param a an Account
     */
    public void createAccount(Account a) {
        // Create a new user with a first and last name

        db.collection("Account")
                .add(a)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    /**
     * Helper function to log into the Application via Database access
     *
     * @param phone_number of the person which wants to login
     */
    public void login(String phone_number) {
        db.collection("Account")
                .whereEqualTo("phone_number", phone_number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Order> order = new ArrayList<>();
                        Storage conf = Storage.getInstance();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // set user_id
                                conf.setUserID((String) document.getId());
                            }
                            if (conf.getUserID() != null) {
                                getMyOrder(conf.getUserID());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Gets all Orders nearby in a specific distance
     */
    public void getOrders() {
        db.collection("Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order o = new Order();
                                o.setId(document.getId());
                                o.setCarNecessary((String) document.get("carNecessary"));
                                o.setHouse_number((String) document.get("house_number"));
                                o.setPhone_number((String) document.get("phone_number"));
                                o.setZip((String) document.get("zip"));
                                o.setStreet((String) document.get("street"));
                                o.setStatus((String) document.get("status"));
                                o.setUrgency((String) document.get("urgency"));
                                o.setHouse_number((String) document.get("house_number"));
                                o.setName((String) document.get("name"));
                                o.setPrescription((String) document.get("prescription"));
                                o.setCarNecessary((String) document.get("carNecessary"));

                                if (document.get("lat") != null && document.get("lng") != null) {
                                    o.setLat((Double) document.get("lat"));
                                    o.setLng((Double) document.get("lng"));
                                }

                                OrderHandler.getInstance().addOrder(o);

                                OrderHandler.getInstance().setLieferant(OrderHandler.Type.Besteller, o.getLat(), o.getLng());

                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    /**
     * Pulls the own Order out of the database and saves it into our Storage-Singelton
     * @param user_id of the person you wanna get the orders
     */
    public void getMyOrder(String user_id) {

        // 1. get order_id form Order_Account

        db.collection("Order_Account")
                .whereEqualTo("account_id", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String orderId = "";
                            for (QueryDocumentSnapshot document1 : task.getResult()) {
                                orderId = (String) document1.get("order_id");
                            }
                            if(orderId != null) {
                                Log.i("TEST", orderId);


                                // 2. get Order and store it in Store
                                getOrder(orderId, order -> {
                                    Log.i("TEST", "TEE" + order.toString());
                                    Storage.getInstance().setCurrentOrder(order);

                                });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * To set the Status of a Order. In case a user confirmed to do a task its "confirmed" and in case he finished the task its "closed"
     *
     * @param orderId where you want to set the Status
     * @param status  confirmed/closed
     * @throws InterruptedException
     */
    public void setOrderStatus(String orderId, Status status) throws InterruptedException {
        if (status == Status.Confirmed) {

            // set Status in Order

            DocumentReference currentOrder = db.collection("Order").document(orderId);
            currentOrder
                    .update("status", "Confirmed")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("oderstatus", "Erf!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

            //  Adds Entry in Account order
            Order_Account orderAccount = new Order_Account();
            orderAccount.setStatus(status.toString());

            orderAccount.setAccount_id(Storage.getInstance().getUserID());
            orderAccount.setOrder_id(orderId);
            db.collection("Order_Account")
                    .add(orderAccount)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Order in der app speichern
                            getMyOrder(Storage.getInstance().getUserID());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                        }
                    });
        } else if (status == Status.Closed) {

            // Update Order_account
            DocumentReference currentOrder = db.collection("Order_Account").document(orderId);
            currentOrder
                    .update("status", "Closed")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

            // Update Order

            DocumentReference order = db.collection("Order").document(orderId);
            order
                    .update("status", "Closed")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("oderstatus", "Erf!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
        }

    }

    public void getOrder(String orderId, final OrderLoadedCallback callback) {
        DocumentReference order = db.collection("Order").document(orderId);
        order.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Order o = new Order();
                        o.setId(document.getId());
                        o.setCarNecessary((String) document.get("carNecessary"));
                        o.setHouse_number((String) document.get("house_number"));
                        o.setZip((String) document.get("zip"));
                        o.setStreet((String) document.get("street"));
                        o.setHouse_number((String) document.get("house_number"));
                        o.setName((String) document.get("name"));
                        o.setPrescription((String) document.get("prescription"));
                        o.setCarNecessary((String) document.get("carNecessary"));
                        o.setStatus((String) document.get("status"));
                        o.setUrgency((String) document.get("urgency"));
                        if (document.get("lat") != null && document.get("lng") != null) {
                            o.setLat((Double) document.get("lat"));
                            o.setLng((Double) document.get("lat"));
                        }
                        // Order Objekt vollständig
                        callback.onOrderLoaded(o);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}