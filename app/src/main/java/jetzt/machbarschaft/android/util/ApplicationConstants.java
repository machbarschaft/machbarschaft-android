package jetzt.machbarschaft.android.util;


/**
 * Util class for constants to replace the Strings in all places.
 */
public final class ApplicationConstants {
    //Constants for the Shared Preferences
    public static final String SHARED_PREF_USERDATA = "userData";
    public static final String SHARED_PREF_USERDATA_LOGGED_IN = "loggedIn";

    //Constants to track the progress
    public static final String SHARED_PREF_ORDER_IN_PROGRESS = "OrderInProgress";
    public static final String SHARED_PREF_ORDER_IN_PROGRESS_KEY = "orderIDin";
    public static final String SHARED_PREF_ORDER_IN_PROGRESS_BOOL = "activeOrderIDbool";
    public static final String SHARED_PREF_ORDER_IN_STEP = "activeOrderStep";

    //Constants to track if the user has read the warnings.
    public static final String SHARED_PREEF_FIRST_ORDER_WARNING = "orderWarn";
    public static final String SHARED_PREEF_FIRST_ORDER_WARNING_KEY = "orderWarnKey";
}
