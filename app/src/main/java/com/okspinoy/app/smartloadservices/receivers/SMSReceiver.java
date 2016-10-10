package com.okspinoy.app.smartloadservices.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.okspinoy.app.smartloadservices.NewMessageActivity;
import com.okspinoy.app.smartloadservices.PasaloadConfirmationMessageActivity;
import com.okspinoy.app.smartloadservices.PasaloadMessageActivity;
import com.okspinoy.app.smartloadservices.helpers.Constants;
import com.okspinoy.app.smartloadservices.helpers.TransactionConstants;
import com.okspinoy.app.smartloadservices.objects.entities.Point;
import com.okspinoy.app.smartloadservices.objects.entities.Transaction;
import io.realm.Realm;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {

  private static final String TAG = "SLS SMSReceiver";

  public SMSReceiver() {

  }

  @Override public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
      smsReader(intent, context);
    }
  }

  private void smsReader(Intent intent, Context context) {

    Bundle bundle = intent.getExtras();

    SmsMessage[] msgs = null;

    if (bundle != null) {
      try {
        Object[] pdus = (Object[]) bundle.get("pdus");
        msgs = new SmsMessage[pdus.length];

        String messageFrom = "";

        String messageBody = "";

        for (int i = 0; i < msgs.length; i++) {
          msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
          messageFrom = msgs[i].getOriginatingAddress();
          messageBody = messageBody + msgs[i].getDisplayMessageBody();
        }

        if (messageFrom.equals("SmartLoad")) {
          variantReceiver(messageBody, context);
          return;
        }

        if (messageFrom.equals("Pasaload") || messageFrom.equals("808")) {
          pasaloadReceiver(messageBody, context);
          return;
        }

        if (messageFrom.equals("7670")) {

          if (messageBody.contains("Normal")
              && messageBody.contains("Special")
              && messageBody.contains("Bronze")
              && messageBody.contains("Silver")
              && messageBody.contains("Gold")) {

            pointsReceiver(messageBody, context);
            return;
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "Exception caught: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  private void variantReceiver(String messageBody, Context context) {
    if (messageBody.contains("rcvd") || messageBody.contains("received")) {
      Transaction transaction = new Transaction();
      transaction.transactionId = System.currentTimeMillis();
      transaction.name = TransactionConstants.NAME_LOAN.toString();
      transaction.createdDate = new Date();
      transaction.transactionType = TransactionConstants.BORROWED.toString();
      transaction.message = messageBody;
      Realm realm = Realm.getDefaultInstance();
      realm.copyToRealmOrUpdate(transaction);
    }

    if (messageBody.contains("availed")) {
      Transaction transaction = new Transaction();
      transaction.transactionId = System.currentTimeMillis();
      transaction.name = TransactionConstants.NAME_PAYMENT.toString();
      transaction.createdDate = new Date();
      transaction.transactionType = TransactionConstants.PAID.toString();
      transaction.message = messageBody;
      Realm realm = Realm.getDefaultInstance();
      realm.copyToRealmOrUpdate(transaction);
    }

    Intent startIntent = new Intent(context, NewMessageActivity.class);
    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startIntent.putExtra(Constants.MESSAGE.toString(), messageBody);
    context.startActivity(startIntent);
  }

  private void pasaloadReceiver(String messageBody, Context context) {
    if (messageBody.contains("YES") || messageBody.contains("yes") || messageBody.contains("Yes")) {
      Intent startIntent = new Intent(context, PasaloadConfirmationMessageActivity.class);
      startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startIntent.putExtra(Constants.MESSAGE.toString(), messageBody);
      context.startActivity(startIntent);
      return;
    }

    Intent startIntent = new Intent(context, PasaloadMessageActivity.class);
    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startIntent.putExtra(Constants.MESSAGE.toString(), messageBody);
    context.startActivity(startIntent);
  }

  private void pointsReceiver(String message, Context context) {
    Point.createNewPoints(message);

    Intent intent = new Intent(Constants.RECEIVER_POINTS.toString());
    context.sendBroadcast(intent);

    Intent startIntent = new Intent(context, NewMessageActivity.class);
    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startIntent.putExtra(Constants.MESSAGE.toString(), message);
    context.startActivity(startIntent);
  }
}
