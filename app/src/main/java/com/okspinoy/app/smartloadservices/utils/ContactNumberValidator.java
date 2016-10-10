package com.okspinoy.app.smartloadservices.utils;

import android.telephony.PhoneNumberUtils;

/**
 * Created by ceosilvajr on 5/18/15.
 */
public class ContactNumberValidator {

    public static boolean isValidPhone(String target) {
        if (target.equals("")) {
            return false;
        } else {
            if (PhoneNumberUtils.isGlobalPhoneNumber(target)) {
                if (target.length() > 8 && target.length() <= 10) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
    
}
