package jetzt.machbarschaft.android.database;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import jetzt.machbarschaft.android.database.callback.WasSuccessfullCallback;

import static com.crashlytics.android.beta.Beta.TAG;

public class Authentication {

    private static Authentication myAuth;
    //private static PhoneAuthProvider phoneAuth;
    private FirebaseAuth auth;

    private FirebaseUser user;
    private String verification_Id;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private Authentication() {
        auth = FirebaseAuth.getInstance();
    }

    public static Authentication getInstance() {
        if (myAuth == null) {
            myAuth = new Authentication();
            //phoneAuth = new PhoneAuthProvider.getInstance();
        }
        return myAuth;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    /**This function is called, when the user enters the confirmation code
     *
     * @param code confirmation code
     * @param callback callback returns true, when the code & the login were successful
     */
    public void verifyCode(String code, Activity a, WasSuccessfullCallback callback) {
        if(this.verification_Id != null) {
            PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(this.verification_Id, code);
            this.signInWithPhoneAuthCredential(credentials, a, callback);
        }
        else {
            callback.wasSuccessful(false);
        }
    }

    /**
     *
     * @param number the phone number which want to login
     * @param smsCodeSent callback returns true, when the sms code was sent
     * @param verificationAndLoginSuccess callback returns true, when the login was successful
     */
    public void verifyNumber(String number, Activity a, WasSuccessfullCallback smsCodeSent, WasSuccessfullCallback verificationAndLoginSuccess) {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                a,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases the phone number can be instantly
                        //     verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                        //     detect the incoming verification SMS and perform verification without
                        //     user action.
                        Log.d(TAG, "onVerificationCompleted:" + credential);

                        signInWithPhoneAuthCredential(credential, a, verificationAndLoginSuccess);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            // ...
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            // ...
                        }

                        // Show a message and update the UI
                        verificationAndLoginSuccess.wasSuccessful(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d(TAG, "onCodeSent:" + verificationId);

                        super.onCodeSent(verificationId, token);
                        // Save verification ID and resending token so we can use them later
                        verification_Id = verificationId;
                        resendToken = token;

                        smsCodeSent.wasSuccessful(true);
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {

                    }

                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, Activity a, WasSuccessfullCallback loginSuccess) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(a, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            user = task.getResult().getUser();
                            // ...
                            loginSuccess.wasSuccessful(true);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    public void updatePhoneNumber() {

    }


}
