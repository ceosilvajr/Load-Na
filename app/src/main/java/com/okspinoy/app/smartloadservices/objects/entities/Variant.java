package com.okspinoy.app.smartloadservices.objects.entities;

import io.realm.Realm;
import io.realm.RealmObject;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public class Variant extends RealmObject {

  public int variantId;
  public String name;
  public String sku;
  public String details;
  public String accessCode;
  public String price;
  public boolean isText;

  public Variant() {
    super();
  }

  public Variant(JSONObject jsonObject) throws JSONException {
    super();
    this.variantId = jsonObject.getInt("id");
    this.name = jsonObject.getString("name");
    this.sku = jsonObject.getString("sku");
    this.details = jsonObject.getString("details");
    this.accessCode = jsonObject.getString("accesscode");
    this.price = jsonObject.getString("price");
    this.isText = jsonObject.getBoolean("isText");
  }

  public static Variant loadById(int variantId) {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(Variant.class).equalTo("variantId", true).findFirst();
  }

  public static List<Variant> getALlVariants() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(Variant.class).findAll();
  }
}
