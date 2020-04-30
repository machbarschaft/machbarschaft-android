package jetzt.machbarschaft.android.util;

import android.content.Context;
import android.content.Intent;

import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;
import jetzt.machbarschaft.android.services.ActiveOrderService;
import jetzt.machbarschaft.android.view.home.HomeActivity;

public class OrderUtil {

    /**
     *  Cancels the current order.
     *  It will be opened again the Database and displayed to other helpers.
     *  Also the notification will disappear and the current steps will be reset in the storage.
     */

    public static void cancelOrder(Order mOrder, Context mContext) {
            DataAccess.getInstance().setOrderStatus(mOrder.getId(), Order.Status.OPEN);
            Storage.getInstance().setCurrentStep(mContext, OrderSteps.STEP0_NONE);
            Storage.getInstance().setActiveOrder(mContext, false);
        Intent serviceIntent = new Intent(mContext, ActiveOrderService.class);
            mContext.stopService(serviceIntent); //stops the foregroundservice
        mContext.startActivity(new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
