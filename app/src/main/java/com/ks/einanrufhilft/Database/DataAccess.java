package com.ks.einanrufhilft.Database;

import com.ks.einanrufhilft.Database.Callback.CollectionLoadedCallback;
import com.ks.einanrufhilft.Database.Callback.DocumentCallback;
import com.ks.einanrufhilft.Database.Entitie.Order;

public class DataAccess extends Database {

    DataAccess() {}


    public enum Status            // Enum-Typ
    {
        Open, Confirmed, Closed;  // Enumerationskonstanten
    }

    public void getOrderById(String orderId, CollectionLoadedCallback callback) {
        super.getDocumentById(CollectionName.Order, orderId, document -> {
            if(document != null) {
                Order order = new Order(document);
                callback.onOrderLoaded(order);
            }
        });
    }

    public void setOrderStatus(String orderId, Status status) {

    }



}
