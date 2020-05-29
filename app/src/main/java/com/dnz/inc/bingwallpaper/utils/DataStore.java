package com.dnz.inc.bingwallpaper.utils;

import android.graphics.Bitmap;

import java.util.Date;


public class DataStore implements Comparable<DataStore> {

    private Bitmap image;
    private Date date;
    private String title;
    private boolean bool;
    private String fullCopyright;
    private String copyright;

    private int _id;

    public DataStore(Bitmap image, Date date, String fullCopyright, boolean bool, int _id) {
        this.image = image;
        this.date = date;
        this.fullCopyright = fullCopyright;
        this.bool = bool;
        this._id = _id;

        setTitle();
    }


    private void setTitle() {
        fullCopyright = fullCopyright.trim();
        String[] temp = fullCopyright.split("\\(|\\)");

        title = temp[0];
        copyright = temp[temp.length - 1];

    }

    public Bitmap getBitmap() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getCopyright() {
        return copyright;
    }

    public boolean getBool() {
        return bool;
    }

    public int get_id() {
        return _id;
    }


    public synchronized void updateBool(boolean bool) {
        this.bool = bool;
    }

    @Override
    public int compareTo(DataStore dataStore) {
        return (int) (dataStore.getDate().getTime() - this.getDate().getTime());
    }
}
