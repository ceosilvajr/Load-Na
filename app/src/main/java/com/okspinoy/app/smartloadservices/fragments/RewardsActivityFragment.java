package com.okspinoy.app.smartloadservices.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.helpers.Constants;
import com.okspinoy.app.smartloadservices.helpers.SMSHelper;
import com.okspinoy.app.smartloadservices.objects.entities.NormalReward;
import com.okspinoy.app.smartloadservices.objects.entities.Point;
import com.okspinoy.app.smartloadservices.receivers.UpdatePointsReceiver;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class RewardsActivityFragment extends Fragment {

    private Activity mContext;

    @InjectView(R.id.tv_total_points)
    TextView mTVTotalPoints;

    @InjectView(R.id.container_normal_rewards)
    LinearLayout mNormalRewardContainer;

    private SMSHelper mSmsHelper;
    private List<NormalReward> mNormalRewards;

    public RewardsActivityFragment() {
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
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        ButterKnife.inject(this, view);
        mContext = getActivity();
        mNormalRewards = NormalReward.getALlNormalReward();

        smsHelperInit();

        displayPoints();

        displayRewards();
        return view;
    }

    private void smsHelperInit() {
        mSmsHelper = new SMSHelper(mContext, new SMSHelper.SMSCallBack() {
            @Override
            public void onError(String message) {
                alert(message);
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

    private void displayRewards() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < mNormalRewards.size(); i++) {
            final NormalReward normalReward = mNormalRewards.get(i);
            View view = layoutInflater.inflate(R.layout.container_normal_rewards, null);
            TextView description = (TextView) view.findViewById(R.id.tv_normal_rewards_description);
            TextView points = (TextView) view.findViewById(R.id.tv_normal_rewards_points);
            Button redeem = (Button) view.findViewById(R.id.btn_normal_rewards);

            description.setText(normalReward.description);
            points.setText("VALID FOR 24 HRS " + normalReward.points + " POINTS");
            redeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(normalReward);
                }
            });

            mNormalRewardContainer.addView(view);
        }
    }

    private void sendMessage(final NormalReward normalReward) {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(mContext.getString(R.string.app_name));
        alertDialog.setMessage("Are you sure you want to redeem " + normalReward.name + " for " + normalReward.points + " points?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.button_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                mSmsHelper.sendMessage(normalReward.accesscode, normalReward.sku);
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

        mTVTotalPoints.setText("" + points);

        if (points < 100) {
            mTVTotalPoints.setTextSize(125);
            return;
        }
        if (points < 1000) {
            mTVTotalPoints.setTextSize(95);
            return;
        }
        if (points < 10000) {
            mTVTotalPoints.setTextSize(60);
            return;
        }
    }

    private void alert(String message) {
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
