package jetzt.machbarschaft.android.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import jetzt.machbarschaft.android.BuildConfig;
import jetzt.machbarschaft.android.database.collections.Order;

public class ReportProblemUtil {

    /**
     * Creates an Intent to open the Email program of the smartphone and adds a standard text template to the message.
     * @return Intent which opens Email
     */
    public static Intent getMailIntent() {
        String mailUri = "mailto:hallo@machbarschaft.jetzt" +
                "?subject=" + "Problem with the Android App" +
                "&body=" + "Dear Machbarschaft team, I noticed a problem with the Android app. The technical information is as follows:" +
                "\nVersion-Name: " + BuildConfig.VERSION_NAME +
                "\nVersion-Code: " + BuildConfig.VERSION_CODE +
                "\nAndroid-Version: " + Build.DISPLAY +
                "\nDevice: " + Build.DEVICE +
                "\nManufacturer: " + Build.MANUFACTURER +
                "\nModel: " + Build.MODEL +
                "\n\n" + "My problem is ...";
        return new Intent(Intent.ACTION_VIEW, Uri.parse(mailUri));
    }

    /**
     * Creates an Intent to open the Phone Action View and call the number of the param order.
     * @param mOrder current Order
     * @return Intent to open Phone Call
     */
    public static Intent callUser(Order mOrder) {
        Uri callUri = Uri.parse("tel:" + (mOrder == null ? "0000000" : mOrder.getPhoneNumber()));
        return new Intent(Intent.ACTION_VIEW, callUri);
    }
}
