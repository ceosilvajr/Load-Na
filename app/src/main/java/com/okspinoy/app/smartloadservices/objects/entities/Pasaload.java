package com.okspinoy.app.smartloadservices.objects.entities;

import io.realm.Realm;
import io.realm.RealmObject;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public class Pasaload extends RealmObject {

  public int pasaloadId;
  public String sku;
  public String description;
  public int srp;
  public int validity;
  public String keyword;
  public String accesscode;

  public Pasaload() {
    super();
  }

  public Pasaload(JSONObject jsonObject) throws JSONException {
    super();
    this.pasaloadId = jsonObject.getInt("id");
    this.sku = jsonObject.getString("sku");
    this.description = jsonObject.getString("description");
    this.srp = jsonObject.getInt("srp");
    this.validity = jsonObject.getInt("validity");
    this.keyword = jsonObject.getString("keyword");
    this.accesscode = jsonObject.getString("accesscode");
  }

  public static Pasaload loadById(int variantId) {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(Pasaload.class).equalTo("pasaloadId", variantId).findFirst();
  }

  public static List<Pasaload> getALlPasaload() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(Pasaload.class).findAll();
  }
}
