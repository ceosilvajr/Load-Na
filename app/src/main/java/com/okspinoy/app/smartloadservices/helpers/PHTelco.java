package com.okspinoy.app.smartloadservices.helpers;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by ceosilvajr on 4/20/15.
 */
public final class PHTelco {

    private static final int PHILIPPINES_COUNTRY_CODE = 515;

    private static final int GLOBE_CODE = 1;

    private static final int SMART_CODE = 3;

    private static final int SUN_CODE = 5;

    private static final int CURE_CODE = 18;

    private static final int NEXTEL_CODE = 88;

    private TelephonyManager mTelephonyManager;

    private String sim1;
    private String deviceId;

    public PHTelco(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        sim1 = mTelephonyManager.getLine1Number();
        deviceId = mTelephonyManager.getDeviceId();
    }

    public String getSim1() {
        return sim1;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getMobileNetworkName() {
        switch (getMobileNetworkCode()) {
            case GLOBE_CODE:
                return "Your are using a Globe network";
            case SMART_CODE:
                return "Your are using a Smart network";
            case SUN_CODE:
                return "Your are using a Sun network";
        }

        return "Mobile network not detected.";
    }

    public boolean isSmart() {
        return isPhilippineTelco() && getMobileNetworkCode() == SMART_CODE;
    }

    public boolean isPhilippineTelco() {
        return getMobileCountryCode() == PHILIPPINES_COUNTRY_CODE;
    }

    private int getMobileNetworkCode() {
        String networkOperator = mTelephonyManager.getNetworkOperator();
        if (networkOperator == null || networkOperator.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(networkOperator.substring(3));
    }

    private int getMobileCountryCode() {
        String networkOperator = mTelephonyManager.getNetworkOperator();
        if (networkOperator == null || networkOperator.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(networkOperator.substring(0, 3));
    }

}
