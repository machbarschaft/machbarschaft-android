package com.ks.einanrufhilft.Database;

import android.os.Message;
import android.util.JsonReader;
import android.util.JsonToken;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Database {
    private FirebaseFirestore db;
    static Database myDBClass;

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

    // Funktioniert

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

    // Funktioniert

    public void getOrders() {

        db.collection("Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Order> order = new ArrayList<>();
                        Database db = Database.getInstance();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                order.add(new Order((String) document.get("phone_number"),
                                        (String) document.get("plz"),
                                        (String) document.get("street"),
                                        (String) document.get("house_number"),
                                        (String) document.get("firstName"),
                                        (String) document.get("lastName"),
                                        (String[]) document.get("category"),
                                        (String) document.get("einkaufszettel"))
                                );
                                if (document.get("first_name") == null) {
                                    Log.i("myOrder", "NULL");
                                } else {
                                    Log.i("myOrder", (String) document.get("first_name"));
                                }
                            }
                            db.allOrders = order;

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public ArrayList<Order> getOrdersNearby(double radius) {


        return null;
    }

    // Funktioniert
    public void setOrderConfirmed(String orderId, Status status) {

        db.collection("Order").document(orderId)
                .update("status", status.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "DocumentSnapshot successfully updated!");
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

