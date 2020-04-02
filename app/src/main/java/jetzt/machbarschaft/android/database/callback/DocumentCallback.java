package jetzt.machbarschaft.android.database.callback;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.UnsupportedEncodingException;

public interface DocumentCallback {
    void onDocumentLoad(DocumentSnapshot document);
}
