package com.okspinoy.app.smartloadservices.objects.entities;

import io.realm.Realm;
import io.realm.RealmObject;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ceosilvajr on 6/11/15.
 */
public class SpecialReward extends RealmObject {

  public int normalRewardId;
  public String name;
  public String description;
  public String sku;
  public String accesscode;

  public SpecialReward() {
    super();
  }

  public SpecialReward(JSONObject jsonObject) throws JSONException {
    super();
    this.normalRewardId = jsonObject.getInt("id");
    this.description = jsonObject.getString("description");
    this.sku = jsonObject.getString("sku");
    this.accesscode = jsonObject.getString("accesscode");
    this.name = jsonObject.getString("name");
  }

  public static SpecialReward loadById(int variantId) {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(SpecialReward.class).equalTo("specialRewardId", variantId).findFirst();
  }

  public static List<SpecialReward> getALlSpecialReward() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(SpecialReward.class).findAll();
  }
}
