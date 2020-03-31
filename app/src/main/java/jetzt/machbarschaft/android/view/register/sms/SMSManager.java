package jetzt.machbarschaft.android.view.register.sms;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 * A class for Manging the SMS registration
 * Use {@link #sendSMS(SMSData, Activity,SMSEventListener)} to send an SMS and
 * {@link #verifySmsCode(SMSData, String, Activity,SMSEventListener)} to verify the code
 */
public abstract class SMSManager
{
    /**
     * If this is true, then the verification with firebase is skipped.
     * So you can type any number and any verification code.
     */
    private static boolean developerMode = true;
    private static SMSManager instance;


    public static SMSManager getInstance()
    {
        if(instance == null)
        {
            instance = developerMode? new SMSManagerDeveloperImpl() : new SMSManagerImpl();
        }
        return instance;
    }


    /**
     * Send an SMS to a User with the given Phone number declared in the {@link SMSData},
     *
     * If the sms was sent {@link SMSEventListener#onSucceedSMSSend(SMSData, Activity)} is called.
     * If the Number is wrong formatted then {@link SMSEventListener#onNumberWrongFormatted(Exception, Activity)} is called.
     * If there is another exception then {@link SMSEventListener#onError(Exception, Activity)} is called.
     */
    abstract public void sendSMS(SMSData smsData, Activity activity,SMSEventListener listener);

    /**
     * Checks if the given Code is the right code for the phone number, given in {@link SMSData#getPhoneNumber()}
     * If the code is right then {@link SMSEventListener#onSucceedLogin(Activity,String)} is called.
     * If not then {@link SMSEventListener#onWrongCode(Exception, Activity)} is called.
     * If there is another exception then {@link SMSEventListener#onError(Exception, Activity)} is called.
     */
    abstract public void verifySmsCode(SMSData smsData,String code,Activity activity,SMSEventListener listener);

    abstract public void signIn(SMSData smsData, PhoneAuthCredential credential, Activity activity,SMSEventListener listener);
}

abstract class FirebaseSmsManger extends SMSManager
{
    @Override
    public void signIn(SMSData smsData, PhoneAuthCredential credential, Activity activity,SMSEventListener listener) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if(task.isSuccessful())
            {
                listener.onSucceedLogin(activity,task.getResult().getUser().getUid());
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
}

class SMSManagerDeveloperImpl extends SMSManagerImpl
{
    @Override
    public void sendSMS(SMSData smsData, Activity activity, SMSEventListener listener) {
        String phoneNumber = "+4915779539312";
        String smsCode = "123456";

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);

        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(
                phoneNumber,
                60L,
                TimeUnit.SECONDS,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        signIn(smsData,credential,activity,listener);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                });
    }
}

class SMSManagerImpl extends FirebaseSmsManger
{
    static PhoneAuthProvider.ForceResendingToken resendingToken;

    SMSManagerImpl(){}

    public void verifySmsCode(SMSData smsData,String code,Activity activity,SMSEventListener listener)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(smsData.getVerificationId(),code);
        signIn(smsData,credential,activity,listener);
    }


    /**
     * {@inheritDoc}
     * {@link SMSManager#sendSMS(SMSData, Activity,SMSEventListener)}
     *
     * Send an SMS via Firebase to the user.
     * The App can auto-detect the SMS for one minute.
     */
    @Override
    public void sendSMS(SMSData smsData,Activity activity,SMSEventListener listener)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                smsData.getPhoneNumber(),
                1,
                TimeUnit.MINUTES,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(smsData,phoneAuthCredential,activity,listener);
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
