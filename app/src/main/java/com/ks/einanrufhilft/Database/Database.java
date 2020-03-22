package com.ks.einanrufhilft.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ks.einanrufhilft.Database.Entitie.Account;
import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.Database.Entitie.Order_Account;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Database {
    private FirebaseFirestore db;
    private static Database myDBClass;

    private ArrayList<Order> allOrders; // spaeter closeOrders

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

    public void createAccount(Account a) {
        // Create a new user with a first and last name

        db.collection("Account")
                .add(a)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    public void login(String phone_numer) {
        Log.i("TestLogin", "***************************:");

        db.collection("Account")
                .whereEqualTo("phone_number", phone_numer)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Order> order = new ArrayList<>();
                        Storage conf = Storage.getInstance();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // user_id setzen
                                conf.setUserId((String) document.getId());
                                Log.i("TestLogin", "Userid: " + conf.getUserId() + "username: " + document.get("first_name"));

                            }
                            if (conf.getUserId() != null) {
                                getMyOrder(conf.getUserId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getOrders() {
        Log.i("TestGetOrders", "***************************:");

        db.collection("Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Order> orders = new ArrayList<>();
                            OrderHandler geo = OrderHandler.getInstance();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Order o = new Order();
                                o.setId(document.getId());
                                o.setCarNecessary((String) document.get("carNecessary"));
                                o.setHouse_number((String) document.get("house_number"));
                                o.setZip((String) document.get("zip"));
                                o.setStreet((String) document.get("street"));
                                Log.i("Order street:", "" + (String) document.get("street"));
                                o.setHouse_number((String) document.get("house_number"));
                                o.setName((String) document.get("name"));
                                o.setPrescription((String) document.get("carNecessary"));
                                o.setCarNecessary((String) document.get("carNecessary"));
                                if (document.get("lat") != null && document.get("lng") != null) {
                                    o.setLat(Double.parseDouble((String) Objects.requireNonNull(document.get("lat"))));
                                    o.setLng(Double.parseDouble((String) Objects.requireNonNull(document.get("lat"))));
                                }

                                orders.add(o);
                                Log.i("Order read:", o.toString());

                                geo.setLieferant(OrderHandler.Type.Besteller, o.getLat(), o.getLng());

                            }
                            Log.i("TES read:", "TEST");


                            Database db = Database.getInstance();
                            db.allOrders = orders;
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    // zieht die eigene Order aus der Datenbank und speichert die in dem Storage Singelton
    public Order getMyOrder(String user_id) {
        db.collection("Order_Account")
                .whereEqualTo("fk_account", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Storage conf = Storage.getInstance();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order o = new Order();
                                o.setId(document.getId());
                                o.setCarNecessary((String) document.get("carNecessary"));
                                o.setHouse_number((String) document.get("house_number"));
                                o.setZip((String) document.get("zip"));
                                o.setStreet((String) document.get("street"));
                                Log.i("Order street:", "" + (String) document.get("street"));
                                o.setHouse_number((String) document.get("house_number"));
                                o.setName((String) document.get("name"));
                                o.setPrescription((String) document.get("carNecessary"));
                                o.setCarNecessary((String) document.get("carNecessary"));
                                if (document.get("lat") != null && document.get("lng") != null) {
                                    o.setLat(Double.parseDouble((String) Objects.requireNonNull(document.get("lat"))));
                                    o.setLng(Double.parseDouble((String) Objects.requireNonNull(document.get("lat"))));
                                }
                                conf.setCurrentOrder(o);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return null;
    }

    // wenn nutzer einen auftrag auswählt ist status = Confirmed
    // wenn nutzer einen Auftrag abgeschlossen hat, ist der status = Closed
    public void setOrderStatus(String orderId, Status status) {
        if (status == Status.Confirmed) {

            //Status in Order anpassen:

            DocumentReference currentOrder = db.collection("Order").document(orderId);
            currentOrder
                    .update("status", "Confirmed")
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

            //  Eintrag in Account_order hinzufügen
            Order_Account orderAccount = new Order_Account();
            orderAccount.setStatus(status.toString());
            orderAccount.setAccount_id(Storage.getInstance().getUserId());
            orderAccount.setOrder_id(orderId);
            db.collection("Order_Account")
                    .add(orderAccount)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Order in der app speichern
                            getMyOrder(Storage.getInstance().getUserId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                        }
                    });
        } else if (status == Status.Closed) {
            DocumentReference currentOrder = db.collection("Order_Account").document(Storage.getInstance().getCurrentOrder().getId());
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
        }

    }
}