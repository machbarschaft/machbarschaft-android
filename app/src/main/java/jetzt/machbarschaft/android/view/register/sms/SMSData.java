package jetzt.machbarschaft.android.view.register.sms;

import java.io.Serializable;

import jetzt.machbarschaft.android.database.entitie.Account;

public class SMSData implements Serializable
{
    /**
     * It's used by firebase to verify the Phone number
     */
    private String verificationId;

    private final Account account;

    public SMSData(Account account) {
        this.account = account;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public Account getAccount()
    {
        return account;
    }

    /**
     * The Phone number from the user
     */
    public String getPhoneNumber() {
        return account.getPhone_number();
    }
}
