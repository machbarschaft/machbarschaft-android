package jetzt.machbarschaft.android.database.callback;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

@Deprecated
public interface DocumentsCallback {
    void onDocumentsLoad(List<DocumentSnapshot> document);
}
