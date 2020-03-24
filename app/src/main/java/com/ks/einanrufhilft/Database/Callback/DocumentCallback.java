package com.ks.einanrufhilft.Database.Callback;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ks.einanrufhilft.Database.Entitie.Order;

public interface DocumentCallback {

        void onDocumentLoad(DocumentSnapshot document);
//        void onOrderLoaded(Order order);
}
