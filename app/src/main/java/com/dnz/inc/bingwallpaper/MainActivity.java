package com.dnz.inc.bingwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.dnz.inc.bingwallpaper.fragments.MainFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Fragment mainFragment, displayImageFragment;
    public static Map<Integer, Object> imageMap = new HashMap<>();


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

        startService(new Intent(this, UpdateService.class));
    }
}
