package com.ks.einanrufhilft.Database;

import com.ks.einanrufhilft.Database.Callback.CollectionLoadedCallback;
import com.ks.einanrufhilft.Database.Callback.DocumentCallback;
import com.ks.einanrufhilft.Database.Entitie.Account;
import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.Database.Entitie.Order_Account;

import java.util.AbstractMap;

public class DataAccess extends Database {

    DataAccess() {
    }

    public enum Status
    {
        Open, Confirmed, Closed;
    }

    public void createAccount(Account account) {
        super.addDocument(CollectionName.Account, account);
    }

    public void getOrderById(String orderId, CollectionLoadedCallback callback) {
        super.getDocumentById(CollectionName.Order, orderId, document -> {
            if (document != null) {
                Order order = new Order(document);
                callback.onOrderLoaded(order);
            }
        });
    }

    public void setOrderStatus(String orderId, Status status) {

        if (status == Status.Confirmed) {
            // update Status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<String, Object>("status", status.toString()));

            //  Adds Entry in Account order
            Order_Account orderAccount = new Order_Account();
            orderAccount.setStatus(status.toString());
            orderAccount.setAccount_id(Storage.getInstance().getUserID());
            orderAccount.setOrder_id(orderId);
            super.addDocument(CollectionName.Order_Account, orderAccount);
        } else if(status == Status.Closed) {
            // update status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<String, Object>("status", status.toString()));

            // update Status in Order_Account
            super.updateDocument(CollectionName.Order_Account, orderId, new AbstractMap.SimpleEntry<String, Object>("status", status.toString()));
        }
    }




}
