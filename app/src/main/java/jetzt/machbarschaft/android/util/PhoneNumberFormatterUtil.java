package jetzt.machbarschaft.android.util;

/**
 * Util class to get uniform Phone Numbers
 */
public class PhoneNumberFormatterUtil {

    /**
     * Adds the Country Code to the Phone Number and removes unnecessary Zeros in Front of the Number,
     * in case the user doesn't know, that there should not follow a Zero directly after Country Code.
     * Also removes Whitespaces with Regex inside of the number.
     * Accepts Phone Numbers in Style of: 0176 11111111, 176 11111111, 17611111111, 176 1111 1111
     * So Phone Numbers will uniform like: <br>
     * +49176222222
     *
     * @return phone Number
     */
    public static String getPhoneNumber(String countryCode, String secondPartNumber) {
        countryCode = countryCode.trim();
        secondPartNumber = secondPartNumber.trim();
        return countryCode + secondPartNumber.replaceAll("^0+", "");
    }
}