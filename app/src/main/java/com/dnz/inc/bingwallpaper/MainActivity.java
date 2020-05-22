package com.dnz.inc.bingwallpaper;

// TODO: MAKE ALL WRITE DB CONNECTIONS THREAD SAFE.... USING MAINACTIVITY.db_conn
// TODO: make desc text alighn to left in image fragment
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.dnz.inc.bingwallpaper.db.DBHelper;
import com.dnz.inc.bingwallpaper.fragments.ImageFragment;
import com.dnz.inc.bingwallpaper.fragments.MainFragment;
import com.dnz.inc.bingwallpaper.fragments.RecyclerAdapterForMainFragment;
import com.dnz.inc.bingwallpaper.net.CallBacks;
import com.dnz.inc.bingwallpaper.utils.Permissions;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements CallBacks.StartFragment {
    private static final String TAG = "MainActivity";
    private Fragment mainFragment, displayImageFragment;

    public static CallBacks.StartFragment startFragment;
    public static FragmentTransaction ft;

    public static DBHelper db_conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFragment = this;
        db_conn = new DBHelper(this);

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
            // TODO: 5/21/20 catch null pointer exception ft (is null)

            ft.replace(R.id.fragment_container, displayImageFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (IllegalStateException | NullPointerException e) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if (requestCode == Permissions.WRITE_STORAGE_PERMISSION &&
       grantResults[0] == PackageManager.PERMISSION_GRANTED){

           RecyclerAdapterForMainFragment.saveCallBack.save();
       }
    }
}
