package com.okspinoy.app.smartloadservices;

import android.os.Bundle;

import com.okspinoy.app.smartloadservices.helpers.SLSActivity;


public class PointsActivity extends SLSActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
    }

    @Override
    public void onHomeButtonClicked() {
        onBackPressed();
    }

}
