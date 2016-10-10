package com.okspinoy.app.smartloadservices.objects.entities;

import io.realm.Realm;
import io.realm.RealmObject;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ceosilvajr on 6/11/15.
 */
public class NormalReward extends RealmObject {

  public int normalRewardId;
  public String name;
  public String description;
  public String sku;
  public int points;
  public String accesscode;

  public NormalReward() {
    super();
  }

  public NormalReward(JSONObject jsonObject) throws JSONException {
    super();
    this.normalRewardId = jsonObject.getInt("id");
    this.description = jsonObject.getString("description");
    this.sku = jsonObject.getString("sku");
    this.points = jsonObject.getInt("points");
    this.accesscode = jsonObject.getString("accesscode");
    this.name = jsonObject.getString("name");
  }

  public static NormalReward loadById(int variantId) {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(NormalReward.class).equalTo("normalRewardId", variantId).findFirst();
  }

  public static List<NormalReward> getALlNormalReward() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(NormalReward.class).findAll();
  }
}
