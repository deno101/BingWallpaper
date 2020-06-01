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
import android.net.ConnectivityManager;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Fragment mainFragment, displayImageFragment;

    public static CallBacks.StartFragment startFragment;
    public static FragmentTransaction ft;

    public static DBHelper db_conn;
    public static RecyclerAdapterForMainFragment.SaveCallBack saveCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }


    private void initFT() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ft = null;
        MainFragment.liveData = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permissions.WRITE_STORAGE_PERMISSION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveCallBack.save();
        }
    }


}
