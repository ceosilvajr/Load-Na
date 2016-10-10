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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.helpers.Constants;
import com.okspinoy.app.smartloadservices.helpers.SMSHelper;
import com.okspinoy.app.smartloadservices.objects.entities.Point;
import com.okspinoy.app.smartloadservices.objects.entities.SpecialReward;
import com.okspinoy.app.smartloadservices.receivers.UpdatePointsReceiver;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class RedeemActivityFragment extends Fragment {

    private SMSHelper mSmsHelper;
    private Activity mContext;

    private List<SpecialReward> mSpecialRewards;

    @InjectView(R.id.container_special_rewards)
    LinearLayout mLLContainerSpecialRewards;

    @InjectView(R.id.tv_bronze_count)
    TextView mTVBronzeCount;
    @InjectView(R.id.tv_silver_count)
    TextView mTVSilverCount;
    @InjectView(R.id.tv_gold_count)
    TextView mTVGoldCount;
    @InjectView(R.id.tv_special_points)
    TextView mTVSpecialPoints;

    @InjectView(R.id.badge)
    ImageView mBadge;

    public RedeemActivityFragment() {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redeem, container, false);
        ButterKnife.inject(this, view);
        mContext = getActivity();
        smsHelperInit();
        displayPoints();
        mSpecialRewards = SpecialReward.getALlSpecialReward();
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

    private void displayPoints() {
        Point point = Point.getLatestPoints();
        int sp = point.special;
        int bc = point.bronzeCoin;
        int sc = point.silverCoin;
        int gc = point.goldCoin;

        if (bc > 0) {
            mBadge.setImageResource(R.drawable.img_badge_bronze);
        }
        if (sc > 0) {
            mBadge.setImageResource(R.drawable.img_badge_silver);
        }
        if (gc > 0) {
            mBadge.setImageResource(R.drawable.img_badge_gold);
        }

        mTVBronzeCount.setText("" + point.bronzeCoin);
        mTVSilverCount.setText("" + point.silverCoin);
        mTVGoldCount.setText("" + point.goldCoin);
        mTVSpecialPoints.setText("" + sp);

        if (sp < 100) {
            mTVSpecialPoints.setTextSize(65);
            return;
        }
        if (sp < 1000) {
            mTVSpecialPoints.setTextSize(55);
            return;
        }
        if (sp < 10000) {
            mTVSpecialPoints.setTextSize(40);
            return;
        }
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
        for (int i = 0; i < mSpecialRewards.size(); i++) {
            final SpecialReward specialReward = mSpecialRewards.get(i);
            View view = layoutInflater.inflate(R.layout.container_special_rewards, null);

            TextView name = (TextView) view.findViewById(R.id.tv_special_rewards_name);
            TextView description = (TextView) view.findViewById(R.id.tv_special_rewards_description);
            ImageView img = (ImageView) view.findViewById(R.id.img_medal);
            Button redeem = (Button) view.findViewById(R.id.btn_normal_rewards);

            name.setText(specialReward.name);
            description.setText(specialReward.description);

            if (specialReward.normalRewardId == 1) {
                img.setImageResource(R.drawable.img_bronze_coin_medium);
            } else if (specialReward.normalRewardId == 2) {
                img.setImageResource(R.drawable.img_silver_coin_medium);
            } else if (specialReward.normalRewardId == 3) {
                img.setImageResource(R.drawable.img_gold_coin_medium);
            } else {
                img.setImageResource(R.drawable.img_medal);
            }

            redeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(specialReward);
                }
            });

            mLLContainerSpecialRewards.addView(view);
        }
    }

    private void sendMessage(final SpecialReward specialReward) {

        String message = "Are you sure you want to redeem ";

        if (specialReward.normalRewardId == 1) {
            message = message + "1 Bronze Coin?";
        } else if (specialReward.normalRewardId == 2) {
            message = message + "1 Silver Coin?";
        } else if (specialReward.normalRewardId == 3) {
            message = message + "1 Gold Coin?";
        } else {
            message = message + specialReward.name + " ?";
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(mContext.getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.button_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                mSmsHelper.sendMessage(specialReward.accesscode, specialReward.sku);
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
