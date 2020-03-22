package com.ks.einanrufhilft.Database;

import com.ks.einanrufhilft.Database.Entitie.Order;

public class Storage {
    private static Storage storage;

    private String userId;
    private Order currentOrderId;
    private Storage() {

    }
    public static Storage getInstance() {
        if(storage == null) {
            storage = new Storage();
        }
        return storage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Order getCurrentOrder() {
        return currentOrderId;
    }

    public void setCurrentOrder(Order currentOrderId) {
        this.currentOrderId = currentOrderId;
    }
}
