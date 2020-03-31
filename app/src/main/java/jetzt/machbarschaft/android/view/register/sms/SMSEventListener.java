package jetzt.machbarschaft.android.view.register.sms;

import android.app.Activity;

public abstract class SMSEventListener
{
    /**
     * Is called when the user has entered the correct code
     */
    public void onSucceedLogin(Activity activity,String userID) {}

    /**
     * Is called when firebase successfully send the SMS to user.
     */
    public void onSucceedSMSSend(SMSData smsData, Activity activity) {}

    /**
     * Is called when the user types a not well formatted number in the form
     */
    public void onNumberWrongFormatted(Exception firebaseException, Activity activity) {}

    /**
     * Is called when the has entered a wrong code
     */
    public void onWrongCode(Exception firebaseException, Activity activity) {}

    /**
     * Is called when an error occurs
     */
    public void onError(Exception firebaseException, Activity activity) {}
}

