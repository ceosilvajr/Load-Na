package com.okspinoy.app.smartloadservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.okspinoy.app.smartloadservices.helpers.Constants;
import com.okspinoy.app.smartloadservices.helpers.SMSHelper;


public class PasaloadConfirmationMessageActivity extends Activity {

    private static final String TAG = "PasaloadConfirmationMessage";
    private boolean isYes = false;
    private SMSHelper mSMsSmsHelper;

    @Override
    protected void onResume() {
        super.onResume();
        mSMsSmsHelper.registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSMsSmsHelper.unRegisterReceiver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        smsHelperInit();

        String message = getIntent().getExtras().getString(Constants.MESSAGE.toString());

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSMsSmsHelper.sendMessage(Constants.PASALOAD_ACCESSCODE.toString(), "YES");
                isYes = true;
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.button_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSMsSmsHelper.sendMessage(Constants.PASALOAD_ACCESSCODE.toString(), "NO");
                isYes = false;
            }
        });
        alertDialog.show();

    }

    private void smsHelperInit() {
        mSMsSmsHelper = new SMSHelper(this, new SMSHelper.SMSCallBack() {
            @Override
            public void onError(String message) {
                alertError(message);
            }

            @Override
            public void onSent() {
                finish();
            }
        });
    }

    private void alertError(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("Failed to send your request. " + message + ". Retry?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                if (isYes) {
                    mSMsSmsHelper.sendMessage(Constants.PASALOAD_ACCESSCODE.toString(), "YES");
                } else {
                    mSMsSmsHelper.sendMessage(Constants.PASALOAD_ACCESSCODE.toString(), "NO");
                }
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel_big), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                startActivity(new Intent(PasaloadConfirmationMessageActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // DO nothing
    }
}
