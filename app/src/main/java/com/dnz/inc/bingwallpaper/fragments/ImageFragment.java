package com.dnz.inc.bingwallpaper.fragments;

import android.graphics.Bitmap;
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

import android.transition.TransitionManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnz.inc.bingwallpaper.MainActivity;
import com.dnz.inc.bingwallpaper.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ImageFragment";
    private GestureDetector mDetector;

    private Bitmap bitmap;
    private String copyright, title, date;
    private ConstraintSet l_one = new ConstraintSet();
    private ConstraintSet l_two = new ConstraintSet();

    private ConstraintLayout mContainer;


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
        initializeUI();

        l_one.clone(mContainer);
        l_two.clone(getContext(), R.layout.fragment_image_2);

        ((ImageView) mContainer.findViewById(R.id.im_fragment_close)).setOnClickListener(this);
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
        switch (view.getId()){
            case R.id.im_fragment_close:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(mContainer);
            }
            if (distanceY > 0 ) {
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
}
