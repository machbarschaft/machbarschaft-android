package jetzt.machbarschaft.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import jetzt.machbarschaft.android.database.entitie.Order;

public class NavigationUtil {

    /**
     * Start maps app to navigate to order address
     */
    public static void navigateToAddress(Order mOrder, Context context) {
        if (mOrder == null) {
            return;
        }

        @SuppressLint("DefaultLocale")
        Uri locationUri = Uri.parse(String.format("geo:%f,%f?q=%s",
                mOrder.getLatitude(), mOrder.getLongitude(), mOrder.getCompleteAddress()));
        context.startActivity(new Intent(Intent.ACTION_VIEW, locationUri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
