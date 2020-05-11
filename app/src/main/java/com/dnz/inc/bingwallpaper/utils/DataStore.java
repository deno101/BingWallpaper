package com.dnz.inc.bingwallpaper.utils;

import android.graphics.Bitmap;


public class DataStore {

    private Bitmap image;
    private String date;
    private String title;
    private boolean bool;
    private String fullCopyright;
    private String copyright;

    public DataStore(Bitmap image, String date, String fullCopyright, boolean bool) {
        this.image = image;
        this.date = date;
        this.fullCopyright = fullCopyright;
        this.bool = bool;

        setTitle();
    }



    private void setTitle(){
        fullCopyright = fullCopyright.trim();
        String[] temp = fullCopyright.split("\\(|\\)");

        title = temp[0];
        copyright = temp[temp.length -1];

    }

    public Bitmap getBitmap() {
        return image;
    }

    public String getDate() {
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
}