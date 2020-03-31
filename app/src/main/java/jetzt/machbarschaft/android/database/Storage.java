package jetzt.machbarschaft.android.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import io.sentry.core.protocol.App;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;
import jetzt.machbarschaft.android.util.ApplicationConstants;

/**
 * This class saves the information about the user account and orders locally.
 */
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
        return FirebaseAuth.getInstance().getUid();
    }


    public Order getCurrentOrder() {
        return currentOrderId;
    }

    public void setCurrentOrder(Order currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    /**
     * Saves the Order Progress in the Shared Preferences
     *
     * @param context        of the Application
     * @param currentOrderID of the Order you want to save
     * @return
     */
    public static boolean setOrderInProgress(Context context, Order currentOrderID) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS, 0);
        Editor editor = pref.edit();
        editor.putString(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS_KEY, new Gson().toJson(currentOrderID));
        return editor.commit();
    }

    /**
     * Loads the order which is currently in progress.
     *
     * @param context of the application
     * @return Order
     */
    public Order getOrderInProgress(Context context) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS, 0);
        return new Gson().fromJson(pref.getString(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS_KEY, null), Order.class);
    }

    /**
     * Checks if there is already an active order.
     *
     * @param context of application
     * @return boolean true if there is an active order, otherwise false.
     */
    public boolean gotActiveOrder(Context context) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS, 0);
        return pref.getBoolean(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS_BOOL, false);
    }

    /**
     * Updates the boolean, if there is a active order. If active order is set to true, it means, that there
     * is currently an active order.
     *
     * @param context        of application
     * @param gotActiveOrder boolean true if there is an active order
     * @return boolean true if the setting of the active order bool worked.
     */
    public boolean setActiveOrder(Context context, Boolean gotActiveOrder) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS, 0);
        Editor editor = pref.edit();
        editor.putBoolean(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS_BOOL, gotActiveOrder);
        return editor.commit();
    }

    public boolean setCurrentStep(Context context, OrderSteps orderStep){
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS, 0);
        Editor editor = pref.edit();
        editor.putString(ApplicationConstants.SHARED_PREF_ORDER_IN_STEP, new Gson().toJson(orderStep));
        return editor.commit();
    }

    public OrderSteps getCurrentStep(Context context){
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.SHARED_PREF_ORDER_IN_PROGRESS,0 );
        return new Gson().fromJson(pref.getString(ApplicationConstants.SHARED_PREF_ORDER_IN_STEP, null), OrderSteps.class);
    }
}