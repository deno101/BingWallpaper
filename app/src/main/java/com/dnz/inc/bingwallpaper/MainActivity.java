package com.dnz.inc.bingwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.dnz.inc.bingwallpaper.fragments.ImageFragment;
import com.dnz.inc.bingwallpaper.fragments.MainFragment;
import com.dnz.inc.bingwallpaper.net.CallBacks;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements CallBacks.StartFragment {
    private static final String TAG = "MainActivity";
    private Fragment mainFragment, displayImageFragment;

    public static CallBacks.StartFragment startFragment;
    public static FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFragment = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int deviceWidthPixels = displayMetrics.widthPixels;

        Log.d(TAG, "onCreate: deviceWidthPixels" + deviceWidthPixels);
        if (ft == null) {
            initFT();
        }
        mainFragment = new MainFragment();
        if (savedInstanceState == null) {
            ft.add(R.id.fragment_container, mainFragment)
                    .commit();
        }

        setAlarm();
    }

    private void setAlarm() {
        Intent intent = new Intent("com.dnz.inc.bingwallpaper.UPDATE_SERVICE");
        intent.putExtra("from", "alarmManger");

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm_created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startImageFragment(Bitmap image, String copyright, String title, String date) {
        displayImageFragment = new ImageFragment(image, copyright, title, date);
        try {
            ft.replace(R.id.fragment_container, displayImageFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            initFT();
            startImageFragment(image, copyright, title, date);
        }
    }

    private void initFT() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ft = null;
    }
}
