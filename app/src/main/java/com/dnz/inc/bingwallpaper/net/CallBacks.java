package com.dnz.inc.bingwallpaper.net;

import android.graphics.Bitmap;

public class CallBacks {
    public interface FragmentCallback{
        void notifyAdapter(int i);
    }

    public interface StartFragment{
        void startImageFragment(Bitmap image, String copyright, String title);
    }
}
