package com.dnz.inc.bingwallpaper.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dnz.inc.bingwallpaper.R;
import com.dnz.inc.bingwallpaper.db.ContractSchema;
import com.dnz.inc.bingwallpaper.db.DBHelper;
import com.dnz.inc.bingwallpaper.utils.DataStore;
import com.dnz.inc.bingwallpaper.utils.FileUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    public static DataStore dataStore = new DataStore();
    private RecyclerAdapterForMainFragment adapter;
    private RecyclerView recyclerView;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize all recycler views
        recyclerView = getActivity().findViewById(R.id.recycler_view_main_fragment);
        adapter = new RecyclerAdapterForMainFragment();

        RecyclerView.LayoutManager layoutManager = new MyLinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    private void getDataFromDB(Context context) {
        new MyAsyncTask(context).execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataFromDB(getActivity());
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        public MyAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DBHelper dbHelper = new DBHelper(context);

            Map<Integer, Object> selectMap = dbHelper.SelectAll();
            for (Integer i : selectMap.keySet()) {
                Map<String, String> dataColumn = (Map<String, String>) selectMap.get(i);

                boolean bool = false;
                String date = dataColumn.get(ContractSchema.ImageDataTable.COLUMN_D_C);
                String isFavorite = dataColumn.get(ContractSchema.ImageDataTable.COLUMN_IS_FAVORITE);
                String title = dataColumn.get(ContractSchema.ImageDataTable.COLUMN_TITLE);

                Log.d(TAG, "doInBackground: title " + title);
                Log.d(TAG, "doInBackground: isFavorite " + isFavorite);
                Log.d(TAG, "doInBackground: date " + date);

                Bitmap image = FileUtils.readImage(context.getFilesDir(), date);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                String dateInFormat = null;
                try {
                    dateInFormat = sdf.format(sdf2.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isFavorite.equals("1")) {
                    bool = true;
                }

                dataStore.insertData(image, dateInFormat, bool, title, i);

                final Integer i2 = i;
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemInserted(i2);
                    }
                });
            }
            return null;
        }
    }

    private class MyLinearLayoutManager extends LinearLayoutManager{

        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }

}
