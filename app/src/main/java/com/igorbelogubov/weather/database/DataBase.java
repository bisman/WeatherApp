package com.igorbelogubov.weather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBase {

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    private final Context mCtx;

    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "weather_info";
    public static final String DB_NAME = "weather";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TEMP = "temp";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_TEMP_MIN = "temp_min";
    public static final String COLUMN_TEMP_MAX = "temp_max";
    public static final String COLUMN_WIND_SPEED = "wind_speed";
    public static final String COLUMN_WIND_DEG = "wind_deg";
    public static final String COLUMN_ICON = "icon";

    public static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_LON + " text, " +
                    COLUMN_LAT + " text, " +
                    COLUMN_DESCRIPTION + " text, " +
                    COLUMN_TEMP + " text, " +
                    COLUMN_PRESSURE + " text, " +
                    COLUMN_HUMIDITY + " text, " +
                    COLUMN_TEMP_MIN + " text, " +
                    COLUMN_TEMP_MAX + " text, " +
                    COLUMN_WIND_SPEED + " text, " +
                    COLUMN_WIND_DEG + " text, " +
                    COLUMN_ICON + " text" +
                    ");";

    public DataBase(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper != null) mDBHelper.close();
    }

    public void addRec(String name, Double lon, Double lat, String description,
                       String temp, String pressure, String humidity, String tempMin,
                       String tempMax, String windSpeed, String windDeg, String icon) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_LON, lon);
        cv.put(COLUMN_LAT, lat);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_TEMP, temp);
        cv.put(COLUMN_PRESSURE, pressure);
        cv.put(COLUMN_HUMIDITY, humidity);
        cv.put(COLUMN_TEMP_MIN, tempMin);
        cv.put(COLUMN_TEMP_MAX, tempMax);
        cv.put(COLUMN_WIND_SPEED, windSpeed);
        cv.put(COLUMN_WIND_DEG, windDeg);
        cv.put(COLUMN_ICON, icon);
        mDB.insert(DB_TABLE, null, cv);
    }

    public Cursor getData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public void delData() {
        mDB.delete(DB_TABLE, null, null);
    }
}
