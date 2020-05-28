package com.dnz.inc.bingwallpaper.net;

import android.graphics.Bitmap;

import com.dnz.inc.bingwallpaper.utils.DataStore;

public class CallBacks {
    public interface FragmentCallback{
        void notifyAdapter(int i);
    }

    public interface StartFragment{
        void startImageFragment(Bitmap image, String copyright, String title, String date);
    }

    public interface FutureTask{
        void run();
    }
}
