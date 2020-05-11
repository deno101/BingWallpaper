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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    public ArrayList<DataStore> dataList = new ArrayList<>();
    private RecyclerAdapterForMainFragment adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerAdapterForMainFragment(this);
        layoutManager = new MyLinearLayoutManager(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize all recycler views
        recyclerView = getActivity().findViewById(R.id.recycler_view_main_fragment);

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

            Cursor cursor = dbHelper.SelectAll();

            if (cursor.getCount() == 0){
                return null;
            }
            int i = 0;
            while(cursor.moveToNext()) {

                boolean bool = false;

                String date = cursor.getString(cursor.getColumnIndex(ContractSchema.ImageDataTable.COLUMN_D_C));
                String isFavorite = cursor.getString(cursor.getColumnIndex(ContractSchema.ImageDataTable.COLUMN_IS_FAVORITE));
                String title = cursor.getString(cursor.getColumnIndex(ContractSchema.ImageDataTable.COLUMN_TITLE));

                Bitmap image = FileUtils.readImage(context.getFilesDir(), date);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                String dateInFormat = null;

                try {
                    dateInFormat = sdf.format(sdf2.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (isFavorite.equals("1")) {
                    bool = true;
                }

                DataStore ds = new DataStore(image, dateInFormat, title, bool);

                dataList.add(ds);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.post(new MyRunnable(i) {
                    @Override
                    public void run() {
                        adapter.notifyItemInserted(this.updateIndex);
                    }
                });

                i++;
            }

            cursor.close();
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

    private abstract class  MyRunnable implements Runnable{
        public int updateIndex;

        public MyRunnable(int updateIndex) {
            this.updateIndex = updateIndex;
        }
    }

}
