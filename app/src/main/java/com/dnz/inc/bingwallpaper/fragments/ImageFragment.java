package com.dnz.inc.bingwallpaper.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dnz.inc.bingwallpaper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {
    private static final String TAG = "ImageFragment";
    private GestureDetector mDetector;

    public ImageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView imageView = getActivity().findViewById(R.id.main_image_for_im_fragment);

        if (mDetector == null){
            mDetector= new GestureDetector(getContext(), new MyGestureListener());
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.d(TAG, "onTouch: fired");
                    mDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
        }

    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll: scroll" + distanceY);
            //TOdo: register view changes.
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}
