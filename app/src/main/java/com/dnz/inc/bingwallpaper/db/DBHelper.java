package com.dnz.inc.bingwallpaper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dnz.inc.bingwallpaper.db.ContractSchema.ImageDataTable;

import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "image_data.db";
    private static final String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MyQueries.CREATE_QUERY_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(MyQueries.DROP_TABLE_QUERY);
        onCreate(sqLiteDatabase);
    }

    public synchronized void insertData(String title, String d_c, String is_favorites) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ImageDataTable.COLUMN_TITLE, title);
        contentValues.put(ImageDataTable.COLUMN_D_C, d_c);
        contentValues.put(ImageDataTable.COLUMN_IS_FAVORITE, is_favorites);

        getWritableDatabase().insert(ImageDataTable.TABLE_NAME, null, contentValues);
    }

    public Cursor SelectAll() {
        return getWritableDatabase().rawQuery(MyQueries.SELECT_ALL_QUERY, null);
    }

    public boolean updateFavorites(String bool, int _id) {
        ContentValues cv = new ContentValues();
        if (!bool.matches("[0-1]")) {
            throw new IllegalArgumentException("bool can only be '1' or '0'");
        }
        cv.put(ImageDataTable.COLUMN_IS_FAVORITE, bool);
        String where = ImageDataTable._ID + " = " + _id;

        int ret = getWritableDatabase().update(ImageDataTable.TABLE_NAME, cv, where, null);
        if (ret == -1) {
            return false;
        }

        return true;
    }
}
