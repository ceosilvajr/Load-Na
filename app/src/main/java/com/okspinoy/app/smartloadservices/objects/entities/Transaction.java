package com.okspinoy.app.smartloadservices.objects.entities;

import android.text.format.DateUtils;
import io.realm.Realm;
import io.realm.RealmObject;
import java.util.Date;
import java.util.List;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public class Transaction extends RealmObject {

  public long transactionId;
  public Date createdDate;
  public String name;
  public String message;
  public String transactionType;

  public Transaction() {
    super();
  }

  public Transaction(long transactionId, Date createdDate, String name, String message,
      String transactionType) {
    super();
    this.transactionId = transactionId;
    this.createdDate = createdDate;
    this.name = name;
    this.message = message;
    this.transactionType = transactionType;
  }

  public static List<Transaction> getAll() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(Transaction.class).findAll();
  }

  public static List<Transaction> getTransactions() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(Transaction.class).findAllSorted("createdDate");
  }

  public String getHumanReadableTime() {
    return DateUtils.getRelativeTimeSpanString(createdDate.getTime(), System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS).toString();
  }
}
