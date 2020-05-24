package com.dnz.inc.bingwallpaper.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dnz.inc.bingwallpaper.MainActivity;
import com.dnz.inc.bingwallpaper.R;
import com.dnz.inc.bingwallpaper.fragments.MainFragment;

import java.util.Timer;
import java.util.TimerTask;

public class Notification {
    private static final String TAG = "Notification";
    public static int LOADING = 1;
    public static int SUCCESS = 2;
    public static int ERROR = 3;
    public static long ANIMATION_DURATION = 400L;
    public  static long WAIT = 1000;

    private static TextView messageView;
    private static ProgressBar progressBar;
    private static View colorView;

    public static void showNotification(int notificationType, String message) {
        try {
            initialize();

            if (notificationType == LOADING) {
                showLoading(message);
            } else if (notificationType == SUCCESS) {
                showSuccess(message);
            } else if (notificationType == ERROR) {
                showError(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showSuccess(String message) {
        colorView.setBackgroundColor(MainFragment.notificationBar.getResources().getColor(R.color.success));
        messageView.setText(message);
        progressBar.setVisibility(View.GONE);
        MainFragment.notificationBar.animate().setListener(new AnimationListener()).
                setDuration(ANIMATION_DURATION).alpha(1).start();
    }

    private static void showLoading(String message) {
        colorView.setBackgroundColor(MainFragment.notificationBar.getResources().getColor(R.color.loading));
        messageView.setText(message);
        progressBar.setVisibility(View.VISIBLE);

        MainFragment.notificationBar.animate().
                setDuration(ANIMATION_DURATION).alpha(1).start();
    }

    private static void showError(String message) {
        colorView.setBackgroundColor(MainFragment.notificationBar.getResources().getColor(R.color.error));
        messageView.setText(message);
        progressBar.setVisibility(View.GONE);

        MainFragment.notificationBar.animate().
                setDuration(ANIMATION_DURATION).alpha(1).setListener(new AnimationListener()).start();
    }

    private static void initialize() {
        progressBar = (ProgressBar) MainFragment.notificationBar.getViewById(R.id.notification_progress_bar);
        colorView = MainFragment.notificationBar.getViewById(R.id.notification_color);
        messageView = (TextView) MainFragment.notificationBar.getViewById(R.id.notification_message);

    }

    private static class AnimationListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationEnd(Animator animation) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    MainFragment.notificationBar.post(new Runnable() {
                        @Override
                        public void run() {
                            MainFragment.notificationBar.animate().
                                    setDuration(ANIMATION_DURATION).alpha(0).start();
                        }
                    });
                }
            }, WAIT);
        }
    }

}
