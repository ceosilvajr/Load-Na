package com.okspinoy.app.smartloadservices;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.okspinoy.app.smartloadservices.helpers.PHTelco;
import com.okspinoy.app.smartloadservices.objects.entities.FAQ;
import com.okspinoy.app.smartloadservices.objects.entities.NormalReward;
import com.okspinoy.app.smartloadservices.objects.entities.Pasaload;
import com.okspinoy.app.smartloadservices.objects.entities.SpecialReward;
import com.okspinoy.app.smartloadservices.objects.entities.Variant;
import com.okspinoy.app.smartloadservices.utils.FileReader;
import io.realm.Realm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends Activity {

  private static final String TAG = "MainActivity";
  private Context mContext;
  private String mAdbLogs;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mContext = this;
    SplashTask splashTask = new SplashTask();
    splashTask.execute();
  }

  private void alert(String messsage) {
    final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
    alertDialog.setCancelable(false);
    alertDialog.setTitle(getString(R.string.app_name));
    alertDialog.setMessage(messsage);
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_close),
        new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
            nextActivity();
          }
        });
    alertDialog.show();
  }

  private boolean dualsimChecker(String log) {
    return log.contains("telephony.registry2") || log.contains("isms2");
  }

  private void saveNormalRewards() throws IOException, JSONException {
    String response = FileReader.readRawFileAsString(this, R.raw.normal_rewards);
    JSONArray jsonArray = new JSONArray(response);
    Realm realm = Realm.getDefaultInstance();
    for (int i = 0; i < jsonArray.length(); i++) {
      final NormalReward normalReward = new NormalReward(jsonArray.getJSONObject(i));
      realm.executeTransaction(new Realm.Transaction() {
        @Override public void execute(Realm realm) {
          realm.copyToRealm(normalReward);
        }
      });
    }
  }

  private void saveFAQs() throws IOException, JSONException {
    String response = FileReader.readRawFileAsString(this, R.raw.faq);
    JSONArray jsonArray = new JSONArray(response);
    Realm realm = Realm.getDefaultInstance();
    for (int i = 0; i < jsonArray.length(); i++) {
      final FAQ faq = new FAQ(jsonArray.getJSONObject(i));
      realm.executeTransaction(new Realm.Transaction() {
        @Override public void execute(Realm realm) {
          realm.copyToRealm(faq);
        }
      });
    }
  }

  private void saveSpecialRewards() throws IOException, JSONException {
    String response = FileReader.readRawFileAsString(this, R.raw.special_rewards);
    JSONArray jsonArray = new JSONArray(response);
    Realm realm = Realm.getDefaultInstance();
    for (int i = 0; i < jsonArray.length(); i++) {
      final SpecialReward specialReward = new SpecialReward(jsonArray.getJSONObject(i));
      realm.executeTransaction(new Realm.Transaction() {
        @Override public void execute(Realm realm) {
          realm.copyToRealm(specialReward);
        }
      });
    }
  }

  private void savePasaloadData() throws IOException, JSONException {
    String response = FileReader.readRawFileAsString(this, R.raw.pasaload);
    JSONArray jsonArray = new JSONArray(response);
    Realm realm = Realm.getDefaultInstance();
    for (int i = 0; i < jsonArray.length(); i++) {
      final Pasaload pasaload = new Pasaload(jsonArray.getJSONObject(i));
      realm.executeTransaction(new Realm.Transaction() {
        @Override public void execute(Realm realm) {
          realm.copyToRealm(pasaload);
        }
      });
    }
  }

  private void saveVariantsData() throws IOException, JSONException {
    String response = FileReader.readRawFileAsString(this, R.raw.variants);
    JSONArray jsonArray = new JSONArray(response);
    Realm realm = Realm.getDefaultInstance();
    for (int i = 0; i < jsonArray.length(); i++) {
      final Variant variant = new Variant(jsonArray.getJSONObject(i));
      realm.executeTransaction(new Realm.Transaction() {
        @Override public void execute(Realm realm) {
          realm.copyToRealm(variant);
        }
      });
    }
  }

  private void nextActivity() {
    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
    startActivity(intent);
    finish();
  }

  @Override public void onBackPressed() {
    // Do nothing
  }

  private class SplashTask extends AsyncTask<Void, Void, Void> {
    @Override protected Void doInBackground(Void... params) {

      try {
        Process process = Runtime.getRuntime().exec("service list");
        BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder log = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          log.append(line + "\n");
        }
        mAdbLogs = log.toString();

        Thread.sleep(2000);

        process.destroy();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

      try {
        savePasaloadData();
        saveVariantsData();
        saveNormalRewards();
        saveSpecialRewards();
        saveFAQs();
      } catch (IOException | JSONException e) {
        e.printStackTrace();
      }

      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      PHTelco phTelco = new PHTelco(mContext);
      if (!phTelco.isSmart()) {
        alert(getString(R.string.alert_only_available_smart));
        Log.d(TAG, phTelco.getMobileNetworkName());
        return;
      }
      nextActivity();
    }
  }
}
