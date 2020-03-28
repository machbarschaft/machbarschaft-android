package jetzt.machbarschaft.android.view.register.sms;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import jetzt.machbarschaft.android.database.entitie.Account;

/**
 * A class for Manging the SMS registration
 * Use @link{#sendSMS(SMSData, Activity)} to send an SMS and
 * {@link #verifySmsCode(SMSData, String, Activity)} to verify the code
 */
public abstract class SMSManager
{
    /**
     * If this is true, then the verification with firebase is skipped.
     * So you can type any number and any verification code.
     */
    private static boolean developerMode = false;
    private static SMSManager instance;

    static PhoneAuthProvider.ForceResendingToken resendingToken;
    static SMSEventListener listener;

    public static SMSManager getInstance()
    {
        if(instance == null)
        {
            instance = developerMode ? new SMSDeveloperImpl() : new SMSManagerImpl();
            listener = new SMSEventListenerImpl();
        }
        return instance;
    }


    /**
     * Send an SMS to a User with the given Phone number declared in the {@link SMSData},
     * If the {@link SMSManager#resendingToken}, not null, then the same sms will be re-sended.
     *
     * If the sms was sent {@link SMSEventListener#onSucceedSMSSend(SMSData, Activity)} is called.
     * If the Number is wrong formatted then {@link SMSEventListener#onNumberWrongFormatted(Exception, Activity)} is called.
     * If there is another exception then {@link SMSEventListener#onError(Exception, Activity)} is called.
     */
    abstract public void sendSMS(SMSData smsData, Activity activity);

    /**
     * Checks if the given Code is the right code for the phone number, given in {@link SMSData#getPhoneNumber()}
     * If the code is right then {@link SMSEventListener#onSucceedLogin(Activity,Account)} is called.
     * If not then {@link SMSEventListener#onWrongCode(Exception, Activity)} is called.
     * If there is another exception then {@link SMSEventListener#onError(Exception, Activity)} is called.
     */
    abstract public void verifySmsCode(SMSData smsData,String code,Activity activity);

}

/**
 * Only for Development
 */
class SMSDeveloperImpl extends  SMSManager
{
    /**
     * Call {@link SMSEventListener#onSucceedSMSSend(SMSData, Activity)} without any checks.
     * So the developer can type any Phone number.
     */
    @Override
    public void sendSMS(SMSData smsData, Activity activity) {
        listener.onSucceedSMSSend(smsData,activity);
    }

    /**
     * Call {@link SMSEventListener#onSucceedLogin(Activity,Account)} without any checks.
     * So the developer can type any Code.
     */
    @Override
    public void verifySmsCode(SMSData smsData, String code, Activity activity) {
        //TODO Login in a Firebase developer account

        listener.onSucceedLogin(activity,null);
    }

}

class SMSManagerImpl extends SMSManager
{
    private FirebaseAuth mAuth;

    SMSManagerImpl()
    {
        mAuth = FirebaseAuth.getInstance();
    }

    public void verifySmsCode(SMSData smsData,String code,Activity activity)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(smsData.getVerificationId(),code);
        signIn(smsData,credential,activity);
    }

    private void signIn(SMSData smsData,PhoneAuthCredential credential,Activity activity)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if(task.isSuccessful())
            {
                listener.onSucceedLogin(activity,smsData.getAccount());
            }
            else
            {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    listener.onWrongCode(task.getException(),activity);
                }
                else
                {
                    listener.onError(task.getException(),activity);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     * {@link SMSManager#sendSMS(SMSData, Activity)}
     *
     * Send an SMS via Firebase to the user.
     * The App can auto-detect the SMS for one minute.
     */
    @Override
    public void sendSMS(SMSData smsData,Activity activity)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                smsData.getPhoneNumber(),
                1,
                TimeUnit.MINUTES,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(smsData,phoneAuthCredential,activity);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        if(e instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            listener.onNumberWrongFormatted(e,activity);
                        }
                        else
                        {
                            listener.onError(e,activity);
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        smsData.setVerificationId(s);
                        resendingToken = forceResendingToken;

                        listener.onSucceedSMSSend(smsData,activity);

                    }
                },
                resendingToken
        );
    }
}
