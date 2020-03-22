package com.ks.einanrufhilft.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;

import com.ks.einanrufhilft.util.ApplicationConstants;

public class Storage {
    private static Storage storage;

    private String userId;
    private String currentOrderId;

    private Storage() {

    }

    public static Storage getInstance() {
        if (storage == null) {
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

    public String getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrderId(String currentOrderId) {
        this.currentOrderId = currentOrderId;
    }


    public boolean setOrderIdInProgress(Context context, String currentOrderId) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDERINPROGRESS, 0);
        Editor editor = pref.edit();
        editor.putString(ApplicationConstants.SHARED_PREF_ORDERINPRGRESS_KEY, currentOrderId);
        return editor.commit();
    }

    public String getOrderIdInPRogress(Context context) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDERINPROGRESS, 0);
        return pref.getString(ApplicationConstants.SHARED_PREF_ORDERINPRGRESS_KEY, null);
    }
}
