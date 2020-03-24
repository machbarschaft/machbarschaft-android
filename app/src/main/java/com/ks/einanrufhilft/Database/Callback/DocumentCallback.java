package com.ks.einanrufhilft.Database.Callback;

import com.google.firebase.firestore.DocumentSnapshot;

public interface DocumentCallback {

    void onDocumentLoad(DocumentSnapshot document);
}
