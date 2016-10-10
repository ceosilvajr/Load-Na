package com.okspinoy.app.smartloadservices.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.okspinoy.app.smartloadservices.R;
import com.okspinoy.app.smartloadservices.adapters.PasaloadAdapter;
import com.okspinoy.app.smartloadservices.helpers.Constants;
import com.okspinoy.app.smartloadservices.helpers.PHTelco;
import com.okspinoy.app.smartloadservices.helpers.SMSHelper;
import com.okspinoy.app.smartloadservices.objects.entities.Pasaload;
import com.okspinoy.app.smartloadservices.utils.ContactNumberValidator;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class PasaloadActivityFragment extends Fragment {

    private static final String TAG = "PasaloadActivity";
    private SMSHelper mSmsHelper;

    @InjectView(R.id.spn_pasa_type)
    Spinner mSpnPasaType;

    @InjectView(R.id.edit_customer_number)
    EditText mEdtCustomerNumber;

    private Activity mActivity;

    private PasaloadAdapter mAdtPasaType;
    private Pasaload mPasaload;
    private List<Pasaload> mPasaloads;

    public PasaloadActivityFragment() {

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

        View view = inflater.inflate(R.layout.fragment_pasaload, container, false);
        ButterKnife.inject(this, view);

        mActivity = getActivity();

        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        smsHelperInit();

        mPasaloads = Pasaload.getALlPasaload();

        mAdtPasaType = new PasaloadAdapter(mActivity, mPasaloads);
        mSpnPasaType.setOnItemSelectedListener(onItemSelectedListener);
        mSpnPasaType.setAdapter(mAdtPasaType);

        return view;
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mPasaload = mPasaloads.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            mPasaload = mPasaloads.get(0);
        }
    };

    private void smsHelperInit() {
        mSmsHelper = new SMSHelper(mActivity, new SMSHelper.SMSCallBack() {
            @Override
            public void onError(String message) {
                alertError(message);
            }

            @Override
            public void onSent() {

            }
        });
    }

    @OnClick(R.id.btn_send)
    void onBtnSendClicked() {

        String number = mEdtCustomerNumber.getText().toString();
        if (!ContactNumberValidator.isValidPhone(number)) {
            alertError("Please input valid contact number.");
            return;
        }
        PHTelco phTelco = new PHTelco(mActivity);
        if (!phTelco.isSmart()) {
            alertError("Please input smart number only.");
            return;
        }
        number = "0" + number;
        String message = number + " " + mPasaload.sku;
        Log.d(TAG, message);

        mSmsHelper.sendMessage(Constants.PASALOAD_ACCESSCODE.toString(), message);
    }

    private void alertError(String message) {
        final AlertDialog alertError = new AlertDialog.Builder(mActivity).create();
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
