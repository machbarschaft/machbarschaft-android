package jetzt.machbarschaft.android.view.register.sms;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class SMSData implements Serializable
{
    /**
     * It's used by firebase to verify the Phone number
     */
    private String verificationId;

    private final String phoneNumber;
}
