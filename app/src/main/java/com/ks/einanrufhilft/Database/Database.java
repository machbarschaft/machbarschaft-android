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
import com.ks.einanrufhilft.Database.Callback.DocumentCallback;
import com.ks.einanrufhilft.Database.Entitie.Collection;

import java.util.AbstractMap;
import java.util.HashMap;

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

    protected  Database() {
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }



    protected void getDocument() {

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

    protected void updateDocument(CollectionName collection, final DocumentCallback callback, AbstractMap.SimpleEntry<String, Object> condition, HashMap<String, Object> updatePair) {}

}