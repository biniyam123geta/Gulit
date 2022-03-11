package com.gulitshopping.shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SqlDatabase extends SQLiteOpenHelper {
    public static final String DbName = "datab";
    public static final String TableName = "value";
    public static final String Col_1 = "Name";
    public static final String Col_2 = "Price";
    public static final String Col_3 = "Descr";
    public static final String Col_4 = "keyl";

    public SqlDatabase(@Nullable Context context) {
        super(context, DbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TableName + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,PRICE TEXT,Descr TEXT,keyl TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }

    public boolean insertdata(String name, String price, String desc, String keyl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_1, name);
        contentValues.put(Col_2, price);
        contentValues.put(Col_3, desc);
        contentValues.put(Col_4, keyl);
        long result = db.insert(TableName, null, contentValues);
        return result != -1;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TableName, null);
        return res;
    }

    public boolean checkIfRecordExist(String chek) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + Col_4 + " FROM " + TableName + " WHERE " + Col_4 + "=='" + chek + "'", null);
            if (cursor.moveToFirst()) {
                // db.close();

                return true;//record Exists

            }
            //db.close();
        } catch (Exception errorException) {
            Log.d("Exception occured", "Exception occured " + errorException);
            // db.close();
        }
        return false;
    }

    public void deletData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableName, "keyl = ?", new String[]{id});

    }
}
