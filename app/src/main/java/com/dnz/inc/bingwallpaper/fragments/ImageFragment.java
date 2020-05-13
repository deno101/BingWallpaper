package com.dnz.inc.bingwallpaper.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnz.inc.bingwallpaper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {
    private static final String TAG = "ImageFragment";
    private GestureDetector mDetector;

    private Bitmap bitmap;
    private String copyright, title, date;


    public ImageFragment(Bitmap bitmap, String copyright, String title, String date) {
        this.bitmap = bitmap;
        this.copyright = copyright;
        this.title = title;
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
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
    }

    private void initializeUI(){
        ImageView imageView = getActivity().findViewById(R.id.main_image_for_im_fragment);

        Log.d(TAG, "initializeUI: ");
        
        if (mDetector == null){
            mDetector= new GestureDetector(getContext(), new MyGestureListener());
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.d(TAG, "onTouch: fired");
                    return true || mDetector.onTouchEvent(motionEvent);
                }
            });
        }

        ((TextView) getActivity().
                findViewById(R.id.image_date_im_fragment)).setText(date);
        ((ImageView) getActivity().
                findViewById(R.id.main_image_for_im_fragment)).setImageBitmap(bitmap);

    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll: scroll" + distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private class MyListener implements LifecycleEventObserver{

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

            Lifecycle.State state = source.getLifecycle().getCurrentState();
            Log.d(TAG, "onStateChanged: "+ state +"  "+event);
            if (event == Lifecycle.Event.ON_START){
                //initializeUI();
            }
        }
    }
}
