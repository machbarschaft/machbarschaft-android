package jetzt.machbarschaft.android.util;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;

import jetzt.machbarschaft.android.BuildConfig;
import jetzt.machbarschaft.android.R;

public class FeedbackMailUtil {


    public static Intent getMailIntent() {
        String mailUri = "mailto:hallo@nachbarschaft.jetzt" +
                "?subject=" + Resources.getSystem().getString(R.string.home_feedback_subject) +
                "&body=" + Resources.getSystem().getString(R.string.home_feedback_body1) +
                "\nVersion-Name: " + BuildConfig.VERSION_NAME +
                "\nVersion-Code: " + BuildConfig.VERSION_CODE +
                "\nAndroid-Version: " + Build.DISPLAY +
                "\nDevice: " + Build.DEVICE +
                "\nManufacturer: " + Build.MANUFACTURER +
                "\nModel: " + Build.MODEL +
                "\n\n" + Resources.getSystem().getString(R.string.home_feedback_body2);
        return new Intent(Intent.ACTION_VIEW, Uri.parse(mailUri));
    }
}
