package com.dnz.inc.bingwallpaper.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dnz.inc.bingwallpaper.R;
import com.dnz.inc.bingwallpaper.db.ContractSchema;
import com.dnz.inc.bingwallpaper.db.DBHelper;
import com.dnz.inc.bingwallpaper.utils.DataStore;
import com.dnz.inc.bingwallpaper.utils.FileUtils;
import com.dnz.inc.bingwallpaper.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    public ArrayList<DataStore> dataList;
    public RecyclerAdapterForMainFragment adapter;
    public RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private View mContainer;

    public static ConstraintLayout notificationBar;

    public static Bundle liveData;
    public ProgressBar progressBar;

    public static MainFragment instance;
    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (liveData != null) {
            return mContainer;
        } else {
            mContainer = inflater.inflate(R.layout.fragment_main, container, false);
            recyclerView = mContainer.findViewById(R.id.recycler_view_main_fragment);
            progressBar = mContainer.findViewById(R.id.progress_main_fragment);
            notificationBar = mContainer.findViewById(R.id.floating_notification);

            liveData = new Bundle();
            createRecyclerView();
        }

        return mContainer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createRecyclerView() {
        new MyAsyncTask(getActivity()).execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        instance = this;

        if (liveData == null){
            createRecyclerView();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        liveData = new Bundle();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        public MyAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DBHelper dbHelper = new DBHelper(context);
            Cursor cursor = dbHelper.SelectAll();

            dataList = new ArrayList<>();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            adapter = new RecyclerAdapterForMainFragment(MainFragment.this);

            layoutManager = new MyLinearLayoutManager(getActivity());
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);
                }
            });


            if (cursor.getCount() == 0) {
                return null;
            }
            int i = 0;
            while (cursor.moveToNext()) {

                boolean bool = false;

                String date = cursor.getString(cursor.getColumnIndex(ContractSchema.ImageDataTable.COLUMN_D_C));
                String isFavorite = cursor.getString(cursor.getColumnIndex(ContractSchema.ImageDataTable.COLUMN_IS_FAVORITE));
                String title = cursor.getString(cursor.getColumnIndex(ContractSchema.ImageDataTable.COLUMN_TITLE));
                int id = cursor.getInt(cursor.getColumnIndex(ContractSchema.ImageDataTable._ID));

                Bitmap image = FileUtils.readImage(context.getFilesDir(), date);
                Date mdate = TimeUtils.getDate(TimeUtils.PATTERN_JSON_DB, date);

                if (isFavorite.equals("1")) {
                    bool = true;
                }

                DataStore ds = new DataStore(image, mdate, title, bool, id);
                dataList.add(ds);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((Activity) context).runOnUiThread(new MyRunnable(i) {
                    @Override
                    public void run() {
                        adapter.insertItem(this.updateIndex);
                    }
                });

                i++;
            }

            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (getActivity() != null) {
                Intent intent = new Intent("com.dnz.inc.bingwallpaper.UPDATE_SERVICE");
                intent.setPackage(getActivity().getPackageName());
                try {
                    getActivity().startService(intent);

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MyLinearLayoutManager extends LinearLayoutManager {

        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    private abstract class MyRunnable implements Runnable {
        public int updateIndex;

        public MyRunnable(int updateIndex) {
            this.updateIndex = updateIndex;
        }
    }

    private class MyLifeCycleObserver implements LifecycleEventObserver {

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            Lifecycle.State currentState = source.getLifecycle().getCurrentState();
            Log.d(TAG, "onStateChanged: state: " + currentState + " event: " + event);
        }

    }
}
