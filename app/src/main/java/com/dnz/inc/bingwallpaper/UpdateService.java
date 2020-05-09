package com.dnz.inc.bingwallpaper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.dnz.inc.bingwallpaper.net.MyRequest;

import androidx.annotation.Nullable;

public class UpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int returnCode = super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "Update service Started", Toast.LENGTH_SHORT).show();

        new MyRequest().makeAPICall(10,this);
        return  returnCode;
    }
}
