package com.dnz.inc.bingwallpaper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.view.ViewGroup;

import com.dnz.inc.bingwallpaper.db.ContractSchema;
import com.dnz.inc.bingwallpaper.db.DBHelper;
import com.dnz.inc.bingwallpaper.net.CallBacks;
import com.dnz.inc.bingwallpaper.net.MyRequest;
import com.dnz.inc.bingwallpaper.utils.FileUtils;
import com.dnz.inc.bingwallpaper.utils.Notification;
import com.dnz.inc.bingwallpaper.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class UpdateService extends Service {

    public static ArrayList<String> dataInDB;
    private static final String CONFIG_FILE = "update_service.conf";
    private static final String LAST_UPDATE_KEY = "LastUpdate";

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

        // get last download date(midnight)  from shared preferences
        String last_update_str = FileUtils.getFromSharedPreferences(this, CONFIG_FILE, LAST_UPDATE_KEY);

        // define callback to update shared preferences
        CallBacks.FutureTask future = new CallBacks.FutureTask() {
            @Override
            public void run() {
                Date now = new Date();
                String lastsave = TimeUtils.forDB_JSON_FS(now);
                FileUtils.writeToSharedPreferences(UpdateService.this,
                        CONFIG_FILE, LAST_UPDATE_KEY, lastsave);
            }
        };

        /*
         * Determine if if (update wallpaper)
         * and number of images to download
         * */
        boolean updateWallpaper = true;
        Date last_update = null;
        int downloadCount = 1;
        if (last_update_str != null) {
            last_update = TimeUtils.getDate(TimeUtils.PATTERN_JSON_DB, last_update_str);
            Calendar now = TimeUtils.getTimeAsOfMidnight();

            if (now.getTimeInMillis() == last_update.getTime()) {
                updateWallpaper = false;
            } else {
                /*
                 * Determine the number of days to download
                 * */
                long difference = -1;
                if ((difference = now.getTimeInMillis() - last_update.getTime()) > 0) {
                    downloadCount = (int) (difference / TimeUtils.ONE_DAY);
                    if (downloadCount < 1) {
                        downloadCount = 1;
                    }
                }
            }
        } else {
            downloadCount = 10;
        }

        if (updateWallpaper) {
            if (internetCheck()) {
                new MyRequest().makeAPICall(downloadCount, this, future);
                Notification.showNotification(Notification.LOADING, "Checking for new wallpapers");
            } else {
                Notification.showNotification(Notification.ERROR, "Error: Check internet Connection");

            }
        } else {
            Notification.showNotification(Notification.SUCCESS, "Wallpapers upto-date");
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
