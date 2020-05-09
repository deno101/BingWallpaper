package com.dnz.inc.bingwallpaper.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

public class NetUtils {

    private static String CORE_URL = "http://www.bing.com";
    private static String JSON_URL = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=%d";

    public static String getJsonUrl(int n) {
        return String.format(JSON_URL, n);
    }

    public static String getJsonUrl(){
        return String.format(JSON_URL, 1);
    }

    public static String getCoreUrl(String imagePath){
        return CORE_URL + imagePath;
    }

    private static RequestQueue requestQueue;

    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext(), new HurlStack());
        }
        return requestQueue;
    }


}
