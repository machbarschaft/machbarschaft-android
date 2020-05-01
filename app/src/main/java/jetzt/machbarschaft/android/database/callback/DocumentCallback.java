package jetzt.machbarschaft.android.database.callback;

import com.google.firebase.firestore.DocumentSnapshot;

@Deprecated
public interface DocumentCallback {
    void onDocumentLoad(DocumentSnapshot document);
}
