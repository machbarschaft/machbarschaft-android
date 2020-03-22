package com.ks.einanrufhilft.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.util.ApplicationConstants;

public class Storage {

    private static Storage storage;

    private String userID;
    private Order currentOrderId;

    private Storage() {


    }

    public static Storage getInstance() {
        if (storage == null) {
            storage = new Storage();
        }
        return storage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID() {
        this.userID = userID;
    }

    public Order getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrderId(Order currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    public boolean setOrderInProgress(Context context, String currentOrderID) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDERINPROGRESS, 0);
        Editor editor = pref.edit();
        editor.putString(ApplicationConstants.SHARED_PREF_ORDERINPRGRESS_KEY, currentOrderID);
        return editor.commit();
    }

    public String getOrderIdInProgress(Context context) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDERINPROGRESS, 0);
        return pref.getString(ApplicationConstants.SHARED_PREF_ORDERINPRGRESS_KEY, null);
    }

}