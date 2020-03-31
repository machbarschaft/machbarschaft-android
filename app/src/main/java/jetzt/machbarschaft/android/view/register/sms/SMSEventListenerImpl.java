package jetzt.machbarschaft.android.view.register.sms;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.view.home.Home;
import jetzt.machbarschaft.android.view.register.VerifyPhoneActivity;

public class SMSEventListenerImpl extends SMSEventListener
{
    /**
     * @inheritDoc
     * @link {SMSEventListener{@link #onSucceedSMSSend(SMSData, Activity)}}
     * Open the HomeActivity.
     * Also, clear the back stack so that the user cannot go back to {@link VerifyPhoneActivity} page when succedful login.
     */
    @Override
    public void onSucceedLogin(Activity activity, String userID)
    {
        Intent i = new Intent(activity, Home.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
    }

    /**
     * @inheritDoc
     * @link {SMSEventListener#onSucceedSMSSend(SMSData, Activity)}}
     *
     * Open the VerifyPhoneActivity.
     */
    @Override
    public void onSucceedSMSSend(SMSData smsData,Activity activity)
    {
        //Open the VerifyPhoneActivity only when it is not already open
        if(!(activity instanceof VerifyPhoneActivity))
        {
            Intent i = new Intent(activity, VerifyPhoneActivity.class);
            i.putExtra("smsData", smsData);
            activity.startActivity(i);
        }
        else
        {
            Toast.makeText(activity, R.string.verify_sms_sent,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNumberWrongFormatted(Exception firebaseException, Activity activity) {
        Toast.makeText(activity, R.string.login_error_invalid_phone_number,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWrongCode(Exception firebaseException, Activity activity) {
        Toast.makeText(activity, R.string.verify_phone_error_invalid_code, Toast.LENGTH_LONG).show();
    }

}
