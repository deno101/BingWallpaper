package com.dnz.inc.bingwallpaper.async;

import android.os.AsyncTask;

import com.dnz.inc.bingwallpaper.db.DBHelper;

public class SaveToDb extends AsyncTask<String, Void, Void> {
    private DBHelper  dbHelper;

    public SaveToDb(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    protected Void doInBackground(String... data) {
        if (data.length != 2){
            throw new IllegalArgumentException("thread takes exactly three arguments");
        }
        String imagedate = data[1];
        String title = data[0];

        dbHelper.insertData(title, imagedate, "0");
        return null;
    }
}
