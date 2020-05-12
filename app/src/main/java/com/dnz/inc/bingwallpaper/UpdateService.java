package com.dnz.inc.bingwallpaper;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import com.dnz.inc.bingwallpaper.db.ContractSchema;
import com.dnz.inc.bingwallpaper.db.DBHelper;
import com.dnz.inc.bingwallpaper.net.MyRequest;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class UpdateService extends Service {

    public static ArrayList<String> dataInDB;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int returnCode = super.onStartCommand(intent, flags, startId);

        if (dataInDB == null){
            dataInDB = new ArrayList<>();
            DBHelper dbHelper = new DBHelper(this);

            Cursor cursor = dbHelper.SelectAll();

            while (cursor.moveToNext()){
                String date = cursor.getString(cursor.getColumnIndex(ContractSchema.ImageDataTable.COLUMN_D_C));
                dataInDB.add(date);
            }
        }


        new MyRequest().makeAPICall(1,this);
        return  returnCode;
    }
}
