package com.okspinoy.app.smartloadservices.helpers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public abstract class SLSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onHomeButtonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void onHomeButtonClicked();
}
