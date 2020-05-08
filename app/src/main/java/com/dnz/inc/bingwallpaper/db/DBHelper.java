package com.dnz.inc.bingwallpaper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dnz.inc.bingwallpaper.MainActivity;
import com.dnz.inc.bingwallpaper.db.ContractSchema.ImageDataTable;

import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "image_data.db";

    public DBHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MyQueries.CREATE_QUERY_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(MyQueries.CREATE_QUERY_IMAGE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void insertData(String title, String d_c, String path, String exp_date, String is_favorites) {
        ContentValues contentValues = new ContentValues();

        // TODO: do this task dynamically using ImageDataTable.COLUMNS[]
        contentValues.put(ImageDataTable.COLUMN_TITLE, title);
        contentValues.put(ImageDataTable.COLUMN_D_C, d_c);
        contentValues.put(ImageDataTable.COLUMN_PATH, path);
        contentValues.put(ImageDataTable.COLUMN_EXP_DATE, exp_date);
        contentValues.put(ImageDataTable.COLUMN_IS_FAVORITE, is_favorites);

        getWritableDatabase().insert(ImageDataTable.TABLE_NAME, null, contentValues);
    }

    public void SelectAll(){
        Cursor cursor = getWritableDatabase().rawQuery(MyQueries.SELECT_ALL_QUERY, null);

        if (cursor.getCount() == 0){
            // Todo: get 10 images from bing
        }
        int i = 0;
        while (cursor.moveToNext()){
            Map<String, Object> map = new HashMap<>();
            for (String columnName : ImageDataTable.COLUMNS){
                map.put(columnName, cursor.getString(cursor.getColumnIndex(columnName)));
            }
            MainActivity.imageMap.put(i, map);
            // TODO: notify Adapter item inserted
            i++;
        }
    }
}
