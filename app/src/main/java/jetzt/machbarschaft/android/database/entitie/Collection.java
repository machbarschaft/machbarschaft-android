package jetzt.machbarschaft.android.database.entitie;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import jetzt.machbarschaft.android.database.callback.DocumentCallback;

import static android.content.ContentValues.TAG;

public abstract class Collection {
/*
    public static <T extends Collection> T getById(Class<T> entityClass, String documentId, DocumentCallback callback) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(entityClass.toString()).document(documentId);
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
        return null;
    };
  */

    // public void write()

}
