package com.okspinoy.app.smartloadservices.objects.entities;

import io.realm.RealmObject;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public class User extends RealmObject {

  public String mobileNumber;
  public String badgeLevel;
  public boolean isActive;
  public int bronzeCoins;
  public int silverCoins;
  public int goldCoins;
}
