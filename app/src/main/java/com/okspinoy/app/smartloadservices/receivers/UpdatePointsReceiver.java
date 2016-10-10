package com.okspinoy.app.smartloadservices.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.okspinoy.app.smartloadservices.helpers.Constants;

/**
 * Created by ceosilvajr on 6/9/15.
 */
public abstract class UpdatePointsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Constants.RECEIVER_POINTS.toString())) {
            update();
        }

    }

    public abstract void update();

}
