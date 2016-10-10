package com.okspinoy.app.smartloadservices.helpers;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public class SMSHelper {

    private static final String TAG = "SMSHelper";
    private static final String SMS_SENT = "SMS_SENT";

    private Activity mActivity;
    private SMSCallBack mSMSmsCallBack;
    private ProgressDialog mProgressDialog;

    public SMSHelper(Activity mActivity, SMSCallBack smsCallBack) {
        this.mActivity = mActivity;
        this.mSMSmsCallBack = smsCallBack;
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Sending request...");
    }

    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mProgressDialog.dismiss();

            if (getResultCode() == Activity.RESULT_OK) {
                Log.d(TAG, "SMS sent");
                mSMSmsCallBack.onSent();
                Toast.makeText(mActivity, "Request sent.", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (getResultCode()) {
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    mSMSmsCallBack.onError("Generic failure, doesn't meet the requirements.");
                    Log.d(TAG, "Generic failure");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    mSMSmsCallBack.onError("No service");
                    Log.d(TAG, "No service");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    mSMSmsCallBack.onError("Null PDU");
                    Log.d(TAG, "Null PDU");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    mSMSmsCallBack.onError("Radio off");
                    Log.d(TAG, "Radio off");
                    break;
                default:
                    mSMSmsCallBack.onError("Failed to sent your request.");
                    break;
            }
        }
    };

    public void sendMessage(String receipient, String message) {
        Log.d(TAG, "Message :" + message + " Sending to " + receipient);
        mProgressDialog.show();
        PendingIntent sentPI = PendingIntent.getBroadcast(mActivity, 0,
                new Intent(SMS_SENT), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(receipient, null, message, sentPI, null);
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(SMS_SENT);
        mActivity.registerReceiver(smsReceiver, intentFilter);
        Log.d(TAG, "BroadcastReceiver Registered");
    }

    public void unRegisterReceiver() {
        mActivity.unregisterReceiver(smsReceiver);
        Log.d(TAG, "BroadcastReceiver Unregistered");
    }

    public interface SMSCallBack {

        void onError(String message);

        void onSent();
    }
}
