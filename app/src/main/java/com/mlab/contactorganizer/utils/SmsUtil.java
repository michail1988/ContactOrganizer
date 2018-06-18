package com.mlab.contactorganizer.utils;

import android.telephony.SmsManager;

public class SmsUtil {
    public static void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
