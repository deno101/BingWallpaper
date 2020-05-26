package com.dnz.inc.bingwallpaper.fragments;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import android.os.Environment;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dnz.inc.bingwallpaper.MainActivity;
import com.dnz.inc.bingwallpaper.R;
import com.dnz.inc.bingwallpaper.utils.FileUtils;
import com.dnz.inc.bingwallpaper.utils.Permissions;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements View.OnClickListener, RecyclerAdapterForMainFragment.SaveCallBack {
    private static final String TAG = "ImageFragment";
    private GestureDetector mDetector;

    private Bitmap bitmap;
    private String copyright, title, date;
    private ConstraintSet l_one = new ConstraintSet();
    private ConstraintSet l_two = new ConstraintSet();

    private ConstraintLayout mContainer;
    public static boolean refreshMain;
    private ImageView likeView;


    public ImageFragment(Bitmap bitmap, String copyright, String title, String date) {
        this.bitmap = bitmap;
        this.copyright = copyright;
        this.title = title;
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mContainer =
                (ConstraintLayout) inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLifecycle().addObserver(new MyListener());

    }

    @Override
    public void onStart() {
        super.onStart();

        if (refreshMain) {
            MainFragment.liveData = null;
            refreshMain = false;
        }
        initializeUI();

        l_one.clone(mContainer);
        l_two.clone(getContext(), R.layout.fragment_image_2);

        mContainer.findViewById(R.id.im_fragment_close).setOnClickListener(this);
        mContainer.findViewById(R.id.image_view_delete).setOnClickListener(this);
        mContainer.findViewById(R.id.image_view_set_wallpaper).setOnClickListener(this);
        likeView = mContainer.findViewById(R.id.image_view_like);

        likeView.setOnClickListener(this);

        mContainer.findViewById(R.id.image_view_save).setOnClickListener(this);
    }

    private void initializeUI() {


        if (mDetector == null) {
            mDetector = new GestureDetector(getContext(), new MyGestureListener());
            mContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
        }

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyymmdd", Locale.getDefault());

        Date mDate = null;
        // TODO: 5/26/20 causes errors when loading from UpdateService
        try {
            mDate = dateFormat1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();

            try {
                mDate = dateFormat2.parse(date);
            } catch (ParseException ex) {
                ex.printStackTrace();
                throw new IllegalArgumentException("invalid date supplied");
            }
        }

        String str = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(mDate);

        ((TextView) getActivity().
                findViewById(R.id.image_date_im_fragment)).setText(str);
        ((ImageView) getActivity().
                findViewById(R.id.main_image_for_im_fragment)).setImageBitmap(bitmap);
        ((TextView) getActivity().
                findViewById(R.id.description_text_view)).setText(title);
        ((TextView) getActivity().
                findViewById(R.id.copyright_text_view)).setText(copyright);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_fragment_close:
                getFragmentManager().popBackStack();
                break;

            case R.id.image_view_like:
                // TODO: 5/26/20 change constructor for imageFragment
                likeView.setImageResource(R.drawable.ic_heart_red);
                break;
            case R.id.image_view_delete:
                MainActivity.db_conn.deleteEntry_byDate(date);
                getFragmentManager().popBackStack();

                MainFragment.liveData = null;
                break;
            case R.id.image_view_save:
                if (Permissions.checkStoragePermission(getActivity())) {
                    save();
                } else {
                    Permissions.getStoragePermissions(getActivity());
                    MainActivity.saveCallBack = this;
                }
                break;
            case R.id.image_view_set_wallpaper:
                new ChangeWallPaper().execute(bitmap);
                break;

        }
    }

    public void save() {
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        root = new File(root, "Bing Wallpapers");

        FileUtils.saveImageToFile(bitmap, root, title + ".jpg");
        MediaScannerConnection.scanFile(getContext(), new String[]{root.toString()},
                null, null);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(mContainer);
            }
            if (distanceY > 0) {
                l_two.applyTo(mContainer);
            } else if (distanceY < 0) {
                l_one.applyTo(mContainer);
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private class MyListener implements LifecycleEventObserver {

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

        }
    }

    private class ChangeWallPaper extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bitmap... voids) {
            WallpaperManager manager = WallpaperManager.getInstance(getContext());

            Bitmap mBitmap = voids[0];

            Display display = getActivity().getWindowManager().getDefaultDisplay();

            int displayHeight = display.getHeight();
            int displayWidth = display.getWidth();


            int imageHeight = mBitmap.getHeight();
            int imageWidth = mBitmap.getWidth();


            int xStart = 0, yStart = 0, xWidth = displayWidth, yHeight = displayHeight;

            if (displayWidth <= imageWidth && displayHeight <= imageHeight) {

                xStart = ((imageWidth / 2) - (displayWidth / 2));
                yStart = ((imageHeight / 2) - (displayHeight / 2));

            } else if (displayWidth <= imageWidth && displayHeight >= imageHeight) {
                int scaledWidth = (int) (imageHeight * (displayWidth / (float) displayHeight));
                xStart = ((imageWidth / 2) - (scaledWidth / 2));

                xWidth = scaledWidth;
                yHeight = imageHeight;
            } else if (displayWidth >= imageWidth && displayHeight <= imageHeight) {
                int scaledHeight = (int) (imageWidth * (displayHeight / (float) displayWidth));
                xStart = ((imageWidth / 2) - (scaledHeight / 2));

                yHeight = scaledHeight;
                xWidth = imageWidth;
            } else if (displayWidth >= imageWidth && displayHeight >= imageHeight) {
                if (displayWidth >= displayHeight) {
                    int scaledHeight = (int) (imageWidth * (displayHeight / (float) displayWidth));
                    xStart = ((imageWidth / 2) - (scaledHeight / 2));

                    yHeight = scaledHeight;
                    xWidth = imageWidth;
                } else {
                    int scaledWidth = (int) (imageHeight * (displayWidth / (float) displayHeight));
                    xStart = ((imageWidth / 2) - (scaledWidth / 2));

                    xWidth = scaledWidth;
                    yHeight = imageHeight;
                }

            }

            mBitmap = Bitmap.createBitmap(mBitmap, xStart, yStart, xWidth, yHeight);

            try {
                manager.setBitmap(mBitmap);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getContext(),
                        "Wallpaper successfully changed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(),
                        "Unable to update wallpaper", Toast.LENGTH_LONG).show();
            }
        }
    }

}
