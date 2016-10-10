package com.okspinoy.app.smartloadservices.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.helpers.SMSHelper;
import com.okspinoy.app.smartloadservices.objects.entities.Transaction;
import com.okspinoy.app.smartloadservices.helpers.TransactionConstants;
import com.okspinoy.app.smartloadservices.objects.entities.Variant;

import io.realm.Realm;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class VariantsActivityFragment extends Fragment {

    private Variant mVariant;

    @InjectView(R.id.radio_group_variants)
    RadioGroup mRGVariants;

    private Activity mActivity;
    private SMSHelper mSmsHelper;

    public VariantsActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mSmsHelper.registerReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSmsHelper.unRegisterReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_variants, container, false);
        ButterKnife.inject(this, view);

        mActivity = getActivity();
        smsHelperInit();
        return view;
    }

    private void smsHelperInit() {
        mSmsHelper = new SMSHelper(mActivity, new SMSHelper.SMSCallBack() {
            @Override
            public void onError(String message) {
                alertError(message);
            }

            @Override
            public void onSent() {
                Transaction transaction = new Transaction(System.currentTimeMillis(), new Date(),
                        mVariant.name, mVariant.name, TransactionConstants.REGISTRATION.toString());
                Realm realm = Realm.getDefaultInstance();
                realm.copyToRealmOrUpdate(transaction);
            }
        });
    }

    @OnClick(R.id.btn_submit)
    void onSubmitBtnClicked() {

        int selectedId = mRGVariants.getCheckedRadioButtonId();

        switch (selectedId) {
            case R.id.radio_allnet:
                mVariant = Variant.loadById(1);
                break;
            case R.id.radio_onnet:
                mVariant = Variant.loadById(2);
                break;
            case R.id.radio_bigtext:
                mVariant = Variant.loadById(3);
                break;
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("Are you sure you want to avail " + mVariant.name + " for PHP " + mVariant.price + " ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.proceed_big), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!mVariant.isText) {
                    call(mVariant.accessCode, mVariant.name);
                } else {
                    mSmsHelper.sendMessage(mVariant.accessCode, mVariant.sku);
                }
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel_big), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void call(String number, String transactionName) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:*" + number));
            startActivity(intent);

            Transaction transaction = new Transaction(System.currentTimeMillis(), new Date(),
                    transactionName, transactionName, TransactionConstants.REGISTRATION.toString());
            Realm realm = Realm.getDefaultInstance();
            realm.copyToRealmOrUpdate(transaction);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            alertError(getString(R.string.call_not_supported_message));
        }
    }

    private void alertError(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
