package com.okspinoy.app.smartloadservices;

import android.app.Application;
import io.realm.Realm;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public class SLSApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Realm.init(this);
  }
}
