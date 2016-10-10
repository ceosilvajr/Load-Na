package com.okspinoy.app.smartloadservices.objects.entities;

import android.text.format.DateUtils;
import io.realm.Realm;
import io.realm.RealmObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ceosilvajr on 6/4/15.
 */
public class Point extends RealmObject {

  public long pointId;
  public int normal;
  public int special;
  public int bronzeCoin;
  public int silverCoin;
  public int goldCoin;
  public Date updatedDate;

  public Point() {
    super();
  }

  public static List<Point> getAll() {
    Realm realm = Realm.getDefaultInstance();
    return realm.where(Point.class).findAll();
  }

  public static Point getLatestPoints() {
    Realm realm = Realm.getDefaultInstance();
    List<Point> points = realm.where(Point.class).findAllSorted("updatedDate");
    if (points.size() > 0) {
      return points.get(0);
    }
    return initialPoint();
  }

  private static Point initialPoint() {
    Point point = new Point();
    point.pointId = System.currentTimeMillis();
    point.updatedDate = new Date();
    point.normal = 0;
    point.special = 0;
    point.bronzeCoin = 0;
    point.silverCoin = 0;
    point.goldCoin = 0;
    return point;
  }

  private static Date getDateFromString(String string) {
    try {
      DateFormat format = new SimpleDateFormat("yyyy-dd-MMM hh:mm:ss", Locale.ENGLISH);
      return format.parse(string);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void createNewPoints(String message) {
    Realm realm = Realm.getDefaultInstance();
    Point point = new Point();
    point.pointId = System.currentTimeMillis();
    point.updatedDate = new Date();
    point.normal =
        Integer.valueOf(StringUtils.substringBetween(message, "You now have ", " Normal"));
    point.special =
        Integer.valueOf(StringUtils.substringBetween(message, "Normal Points, ", " Special"));
    point.bronzeCoin =
        Integer.valueOf(StringUtils.substringBetween(message, "Special Points, ", " Bronze"));
    point.silverCoin =
        Integer.valueOf(StringUtils.substringBetween(message, "Bronze Coins, ", " Silver"));
    point.goldCoin =
        Integer.valueOf(StringUtils.substringBetween(message, "Silver Coins, ", " Gold"));
    realm.copyToRealm(point);
  }

  public String getHumanReadableTime() {
    return DateUtils.getRelativeTimeSpanString(updatedDate.getTime(), System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS).toString();
  }

  @Override public String toString() {
    return "Normal " + normal + "Date " + updatedDate.toString();
  }
}