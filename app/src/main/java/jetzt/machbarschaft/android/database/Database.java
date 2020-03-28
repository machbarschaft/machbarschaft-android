package jetzt.machbarschaft.android.database;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.security.auth.callback.Callback;

import jetzt.machbarschaft.android.database.callback.DocumentCallback;
import jetzt.machbarschaft.android.database.callback.DocumentsCallback;
import jetzt.machbarschaft.android.database.callback.WasSuccessfullCallback;
import jetzt.machbarschaft.android.database.entitie.Collection;
import jetzt.machbarschaft.android.view.login.LoginMain;

import static android.content.ContentValues.TAG;

/**
 * All database accesses are made here.
 */
public class Database {
    private FirebaseFirestore db;

    //TODO: -> Database abstract class
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
        Log.i("DataAccessTest",condition.toString());
        db.collection(collection.toString())
                .whereEqualTo(condition.getKey(), condition.getValue())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().getDocuments().isEmpty()) {
                            Log.i("DataAccessTest", task.getResult().getDocuments().toString());
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
                        if (task.isSuccessful()&& !task.getResult().getDocuments().isEmpty()) {
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
    protected void getDocumentsByTwoConditions(CollectionName collection, LinkedHashMap<String, Object> condition, final DocumentsCallback callback) {

        ArrayList<String> keys = new ArrayList<String>(condition.keySet());
        ArrayList<Object> values = new ArrayList<Object>(condition.values());
        db.collection(collection.toString())
                .whereEqualTo(keys.get(0), values.get(0))
                .whereEqualTo(keys.get(1), values.get(1))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()&& !task.getResult().getDocuments().isEmpty()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            if (documents.size() > 0) {
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
        DocumentReference docRef = db.collection(collection.toString()).document(documentId);
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

    public void addDocument(CollectionName collectionName, Collection document, WasSuccessfullCallback callback) {
        db.collection(collectionName.toString())
                .add(document)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if (callback != null) {
                            callback.wasSuccessful(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (callback != null) {
                            callback.wasSuccessful(false);
                        }
                    }
                });
    }

    public void addDocument(CollectionName collectionName, Collection document) {
        addDocument(collectionName, document, null);
    }

    protected void updateDocument(CollectionName collection, String documentId, AbstractMap.SimpleEntry<String, Object> updatePair) {
        updateDocument(collection, documentId, updatePair, null);
    }

    // callback can be added
    protected void updateDocument(CollectionName collection, String documentId, AbstractMap.SimpleEntry<String, Object> updatePair, final WasSuccessfullCallback callback) {

        DocumentReference docRef = db.collection(collection.toString()).document(documentId);
        //Log.i(docRef.getId());
        docRef.update(updatePair.getKey(), updatePair.getValue()).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (callback != null) {

                            callback.wasSuccessful(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (callback != null) {
                            callback.wasSuccessful(false);
                        }
                    }
                });

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