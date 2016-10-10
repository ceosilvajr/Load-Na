package com.okspinoy.app.smartloadservices.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.objects.entities.User;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public class UserManager {

    private static final String TAG = "UserManager";

    public static User get(Context context) {
        Gson gson = new Gson();
        Resources res = context.getResources();
        SharedPreferences myPrefs = context.getSharedPreferences(
                res.getString(R.string.pks_package), 0);
        String json = myPrefs.getString("User", "");
        Log.i(TAG, "" + json);
        return gson.fromJson(json, User.class);
    }

    public static void save(User customerLocation, Context context) {
        Gson gson = new Gson();
        Resources res = context.getResources();
        SharedPreferences myPrefs = context.getSharedPreferences(
                res.getString(R.string.pks_package), 0);
        SharedPreferences.Editor e = myPrefs.edit();
        String json = gson.toJson(customerLocation);
        e.putString("User", json);
        e.apply();
        Log.i("User saved", "" + json);
    }

    public static void delete(Context context) {
        Resources res = context.getResources();
        SharedPreferences myPrefs = context.getSharedPreferences(
                res.getString(R.string.pks_package), 0);
        SharedPreferences.Editor e = myPrefs.edit();
        e.remove("User");
        e.apply();
    }

}
