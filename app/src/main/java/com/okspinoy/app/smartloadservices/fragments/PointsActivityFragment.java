package com.okspinoy.app.smartloadservices.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.RedeemActivity;
import com.okspinoy.app.smartloadservices.RewardsActivity;
import com.okspinoy.app.smartloadservices.helpers.Constants;
import com.okspinoy.app.smartloadservices.helpers.SMSHelper;
import com.okspinoy.app.smartloadservices.objects.entities.Point;
import com.okspinoy.app.smartloadservices.receivers.UpdatePointsReceiver;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class PointsActivityFragment extends Fragment {

    private static final String TAG = "PointsActivityFragment";

    private Activity mContext;
    private SMSHelper mSmsHelper;

    @InjectView(R.id.tv_total_points)
    TextView mTVPoints;

    public PointsActivityFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        displayPoints();
        mSmsHelper.registerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.RECEIVER_POINTS.toString());
        mContext.registerReceiver(updatePointsReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSmsHelper.unRegisterReceiver();
        mContext.unregisterReceiver(updatePointsReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_points, container, false);
        ButterKnife.inject(this, view);

        mContext = getActivity();

        smsHelperInit();
        displayPoints();

        for (Point p : Point.getAll()) {
            Log.d(TAG, "Point " + p.toString());
        }

        return view;
    }

    private void smsHelperInit() {
        mSmsHelper = new SMSHelper(mContext, new SMSHelper.SMSCallBack() {
            @Override
            public void onError(String message) {
                alertError(message);
            }

            @Override
            public void onSent() {

            }
        });
    }

    @OnClick(R.id.btn_refresh)
    void refreshBtnClicked() {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("This feature will charge you Php 1.00. Are you sure you want to proceed?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes_big), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                mSmsHelper.sendMessage(Constants.SOS_SERVICE_ACCESSCODE.toString(), Constants.SOS_POINTS.toString());
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.button_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @OnClick(R.id.btn_paynow)
    void paynowBtnClicked() {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("Are you sure you want to pay now?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes_big), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                mSmsHelper.sendMessage(Constants.PAY_NOW_ACCESSCODE.toString(), Constants.PAY.toString());
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.button_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @OnClick({R.id.btn_normal_rewards, R.id.btn_special_rewards})
    void buttonClicked(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.btn_normal_rewards:
                intent = new Intent(mContext, RewardsActivity.class);
                break;

            case R.id.btn_special_rewards:
                intent = new Intent(mContext, RedeemActivity.class);
                break;
        }

        startActivity(intent);

    }

    private UpdatePointsReceiver updatePointsReceiver = new UpdatePointsReceiver() {
        @Override
        public void update() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    displayPoints();
                }
            });
        }
    };

    private void displayPoints() {

        int points = Point.getLatestPoints().normal;

        mTVPoints.setText("" + points);

        if (points < 100) {
            mTVPoints.setTextSize(125);
            return;
        }
        if (points < 1000) {
            mTVPoints.setTextSize(95);
            return;
        }
        if (points < 10000) {
            mTVPoints.setTextSize(60);
            return;
        }
    }

    private void alertError(String message) {
        final AlertDialog alertError = new AlertDialog.Builder(mContext).create();
        alertError.setCancelable(false);
        alertError.setTitle(getString(R.string.app_name));
        alertError.setButton(android.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.button_close),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertError.dismiss();
                    }
                });
        alertError.setMessage(message);
        alertError.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
