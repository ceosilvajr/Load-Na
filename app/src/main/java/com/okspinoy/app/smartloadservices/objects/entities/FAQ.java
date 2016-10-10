package com.okspinoy.app.smartloadservices.objects.entities;

import io.realm.Realm;
import io.realm.RealmObject;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ceosilvajr on 6/18/15.
 */
public class FAQ extends RealmObject {

  public int faqId;
  public boolean isEnglish;
  public String question;
  public String answer;

  public FAQ() {
    super();
  }

  public FAQ(JSONObject jo) throws JSONException {
    this.faqId = jo.getInt("id");
    this.isEnglish = jo.getBoolean("isEnglish");
    this.question = jo.getString("question");
    this.answer = jo.getString("answer");
  }

  public static List<FAQ> getAllEnglish() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(FAQ.class).equalTo("isEnglish", true).findAll();
  }

  public static List<FAQ> getAllTagalog() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(FAQ.class).equalTo("isEnglish", false).findAll();
  }
}
