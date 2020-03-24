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
import com.ks.einanrufhilft.Database.Callback.DocumentCallback;
import com.ks.einanrufhilft.Database.Callback.DocumentsCallback;
import com.ks.einanrufhilft.Database.Entitie.Collection;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

    enum CollectionName {
        Order,
        Account,
        Order_Account
    }

    protected Database() {
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }


    protected void getOneDocumentByCondition(CollectionName collection, AbstractMap.SimpleEntry<String, Object> condition, final DocumentCallback callback) {

        db.collection(collection.toString())
                .whereEqualTo(condition.getKey(), condition.getValue())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            if (document.exists()) {
                                callback.onDocumentLoad(document);
                            } else {
                                callback.onDocumentLoad(null);
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    protected void getOneDocumentByTwoConditions(CollectionName collection, LinkedHashMap<String, Object> condition, final DocumentCallback callback) {

        ArrayList<String> keys = new ArrayList<String>(condition.keySet());
        ArrayList<Object> values = new ArrayList<Object>(condition.values());

        db.collection(collection.toString())
                .whereEqualTo(keys.get(0), values.get(0))
                .whereEqualTo(keys.get(1), values.get(1))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            if (document.exists()) {
                                callback.onDocumentLoad(document);
                            } else {
                                callback.onDocumentLoad(null);
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    protected void getDocumentsByCondition(CollectionName collection, AbstractMap.SimpleEntry<String, Object> condition, final DocumentsCallback callback) {

        db.collection(collection.toString())
                .whereEqualTo(condition.getKey(), condition.getValue())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            if (documents != null) {
                                callback.onDocumentsLoad(documents);
                            } else {
                                callback.onDocumentsLoad(null);
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    protected void getDocumentById(CollectionName collection, String documentId, final DocumentCallback callback) {
        DocumentReference docRef = db.collection(collection.toString()).document("documentId");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        callback.onDocumentLoad(document);
                    } else {
                        Log.d(TAG, "No such document");
                        callback.onDocumentLoad(null);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void addDocument(CollectionName collectionName, Collection document) {
        db.collection(collectionName.toString())
                .add(document)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    protected void updateDocument(CollectionName collection, String documentId, AbstractMap.SimpleEntry<String, Object> updatePair) {
        updateDocument(collection, documentId, null, updatePair);
    }

    // callback can be added
    protected void updateDocument(CollectionName collection, String documentId, final DocumentCallback callback, AbstractMap.SimpleEntry<String, Object> updatePair) {
        DocumentReference docRef = db.collection(collection.toString()).document("documentId");
        docRef.update(updatePair.getKey(), updatePair.getValue());
    }

    protected void updateDocument(CollectionName collection, final DocumentCallback callback, AbstractMap.SimpleEntry<String, Object> condition, HashMap<String, Object> updatePair) {
    }

    protected void getCollection(CollectionName collectionName, final DocumentsCallback callback) {

        db.collection("Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            callback.onDocumentsLoad(documents);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}