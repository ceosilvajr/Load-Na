package com.okspinoy.app.smartloadservices.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.okspinoy.app.smartloadservices.FAQActivity;
import com.okspinoy.app.smartloadservices.PasaloadActivity;
import com.okspinoy.app.smartloadservices.PointsActivity;
import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.TransactionActivity;
import com.okspinoy.app.smartloadservices.VariantsActivity;
import com.okspinoy.app.smartloadservices.objects.entities.Point;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment {

    private Activity mActivity;

    @InjectView(R.id.img_badge)
    ImageView mIVBadge;

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        displayBadge();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayBadge();
    }

    private void displayBadge() {
        Point point = Point.getLatestPoints();
        
        int bc = point.bronzeCoin;
        int sc = point.silverCoin;
        int gc = point.goldCoin;

        if (bc > 0) {
            mIVBadge.setImageResource(R.drawable.img_badge_bronze);
        }
        if (sc > 0) {
            mIVBadge.setImageResource(R.drawable.img_badge_silver);
        }
        if (gc > 0) {
            mIVBadge.setImageResource(R.drawable.img_badge_gold);
        }
    }


    @OnClick({R.id.btn_variants, R.id.btn_points, R.id.btn_transactions, R.id.btn_pasaload, R.id.tv_faq})
    void buttonClicked(Button btn) {

        Intent intent = null;

        switch (btn.getId()) {
            case R.id.btn_variants:
                intent = new Intent(mActivity, VariantsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_points:
                intent = new Intent(mActivity, PointsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_pasaload:
                intent = new Intent(mActivity, PasaloadActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_transactions:
                intent = new Intent(mActivity, TransactionActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_faq:
                intent = new Intent(mActivity, FAQActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
