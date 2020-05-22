package com.dnz.inc.bingwallpaper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

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

        if (dataInDB == null) {
            dataInDB = new ArrayList<>();
            DBHelper dbHelper = new DBHelper(this);

            Cursor cursor = dbHelper.SelectAll();

            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(ContractSchema.ImageDataTable.COLUMN_D_C));
                dataInDB.add(date);
            }
        }

        if (internetCheck()) {
            new MyRequest().makeAPICall(1, this);
        }else {
            Toast.makeText(this, "Check your internet Connection", Toast.LENGTH_LONG).show();
        }
        return returnCode;
    }

    private boolean internetCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
        return true;
    }
}
