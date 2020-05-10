package com.dnz.inc.bingwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.dnz.inc.bingwallpaper.fragments.MainFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Fragment mainFragment, displayImageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int deviceWidthPixels = displayMetrics.widthPixels;

        Log.d(TAG, "onCreate: deviceWidthPixels" + deviceWidthPixels);

        mainFragment = new MainFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mainFragment)
                    .commit();
        }

        Intent intent = new Intent("com.dnz.inc.bingwallpaper.UPDATE_SERVICE");
        intent.setPackage(getPackageName());
        //startService(intent);

        setAlarm();
    }

    private void setAlarm() {
        Intent intent = new Intent("com.dnz.inc.bingwallpaper.UPDATE_SERVICE");
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent == null){
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm_created", Toast.LENGTH_SHORT).show();
    }
}
