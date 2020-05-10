package com.dnz.inc.bingwallpaper.utils;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class DataStore {

    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();
    private Map<Integer, String> dateMap = new HashMap<>();
    private Map<Integer, Boolean> booleanMap = new HashMap<>();
    private Map<Integer, String> titleMap = new HashMap<>();

    public synchronized void insertData(Bitmap bitmap, String date, Boolean isFavorite,
                                        String title, int position){

        bitmapMap.put(position, bitmap);
        dateMap.put(position, date);
        booleanMap.put(position, isFavorite);
        titleMap.put(position, title);
    }

    public Bitmap getBitmap(int position){
        return bitmapMap.get(position);
    }

    public String getDate(int position){
        return dateMap.get(position);
    }

    public Boolean getBoolean(int position){
        return booleanMap.get(position);
    }

    public int length(){
        return booleanMap.size();
    }
    public String getTitle(int position){
        return titleMap.get(position);
    }
}
