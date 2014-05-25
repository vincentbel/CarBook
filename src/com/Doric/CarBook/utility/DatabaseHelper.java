package com.Doric.CarBook.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat 标签
    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    // 数据库名称
    private static final String DATABASE_NAME = "carbook";

    /***********     表名    *******************/
    private static final String TABLE_USER = "user";
    private static final String TABLE_COLLECTION = "collection";


    /*****************    各表的列名      *************************/

    // 通用列名
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "create_at";

    // user表 - 列名
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_USER_ID = "user_id";

    // collection表 - 列名
    public static final String KEY_COLLECTION_CAR_ID = "car_id";
    public static final String KEY_COLLECTION_UER_ID = "user_id";

    /*************** 建表语句    *********************/

    // user表 - 建表语句
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER,"  + KEY_USER_NAME
            + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    // collection表 - 建表语句
    private static final String CREATE_TABLE_COLLECTION = "CREATE TABLE "
            + TABLE_COLLECTION + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COLLECTION_UER_ID
            + " INTEGER," + KEY_COLLECTION_CAR_ID + " INTEGER," + KEY_CREATED_AT + " DATETIME" + ")";


    //默认用户id，当用户没有登录时，收藏汽车时使用
    private static final int DEFAULT_USER_ID = -1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 建表
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_COLLECTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION);
        // create new tables
        onCreate(db);
    }

    // ------------------------ "user" table methods ----------------//

    /*
     *store user info in database
     */
    public long addUser(String userName, int uid, String createAt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, userName);
        values.put(KEY_USER_ID, uid);
        values.put(KEY_CREATED_AT, createAt);

        // insert row
        long id = db.insert(TABLE_USER, null, values);
        db.close();
        return id;
    }

    /*
     * get user details
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            user.put(KEY_USER_NAME, cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
            user.put(KEY_CREATED_AT, cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
            user.put(KEY_USER_ID, cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
        }
        cursor.close();
        db.close();
        return user;
    }

    /*
     * getting user count
     */
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public boolean isCarCollected(int carId) {
        String countQuery = "SELECT  * FROM " + TABLE_COLLECTION + " WHERE " + KEY_COLLECTION_CAR_ID + "=" + carId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return (count > 0);
    }

    public long addCollection(int userId, int carId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COLLECTION_UER_ID, userId);
        values.put(KEY_COLLECTION_CAR_ID, carId);
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long id = db.insert(TABLE_COLLECTION, null, values);
        db.close();
        return id;
    }

    public long addCollection(int carId) {
        return addCollection(DEFAULT_USER_ID, carId);
    }

    public int deleteCollection(int userId, int carId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = KEY_COLLECTION_UER_ID + "=" + userId + " AND " + KEY_COLLECTION_CAR_ID + "=" + carId;

        // insert row
        int id = db.delete(TABLE_COLLECTION, whereClause, null);
        db.close();
        return id;
    }

    public int deleteCollection(int carId) {
        return deleteCollection(DEFAULT_USER_ID, carId);
    }


    public void addUserIdToCollection(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COLLECTION_UER_ID, userId);
        db.update(TABLE_COLLECTION, values, null, null);
    }

    public void resetCollection() {
        addUserIdToCollection(DEFAULT_USER_ID);
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();
    }

    /**
     * get datetime
     */
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
