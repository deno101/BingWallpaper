package com.dnz.inc.bingwallpaper.net;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dnz.inc.bingwallpaper.UpdateService;
import com.dnz.inc.bingwallpaper.db.ContractSchema;
import com.dnz.inc.bingwallpaper.db.DBHelper;
import com.dnz.inc.bingwallpaper.fragments.MainFragment;
import com.dnz.inc.bingwallpaper.utils.DataStore;
import com.dnz.inc.bingwallpaper.utils.FileUtils;
import com.dnz.inc.bingwallpaper.db.ContractSchema.ImageDataTable;
import com.dnz.inc.bingwallpaper.utils.Notification;
import com.dnz.inc.bingwallpaper.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyRequest {
    private static final String TAG = "MyRequest";

    public void makeAPICall(int n, final Context appContext, final CallBacks.FutureTask futureTask) {
        final DBHelper dbHelper = new DBHelper(appContext);
        JsonObjectRequest apiRequest = new JsonObjectRequest(NetUtils.getJsonUrl(n), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Notification.showNotification(Notification.SUCCESS, "Loading Wallpapers");
                            JSONArray jsonArray = response.getJSONArray("images");

                            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String imageDate = jsonObject.getString("startdate");

                                boolean hasMatch = false;
                                for (String s : UpdateService.dataInDB) {
                                    if (s.equals(imageDate)) {
                                        hasMatch = true;
                                        break;
                                    }
                                }

                                if (hasMatch) {
                                    continue;
                                }
                                String title = jsonObject.getString("copyright").trim();
                                String fullImgURL = NetUtils.getCoreUrl(jsonObject.getString("url"));

                                getImage(appContext, dbHelper, fullImgURL, appContext, imageDate, title);
                                UpdateService.dataInDB.add(imageDate);


                            }

                            futureTask.run();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Notification.showNotification(Notification.ERROR, "Error: Check your internet Connection");
                    }
                });

        NetUtils.getRequestQueue(appContext).add(apiRequest);
    }

    private void getImage(final Context appContext, final DBHelper dbHelper, final String fullImgURL, final Context context, String imageDate
            , final String fullTitle) {

        ImageRequest imageRequest = new ImageRequest(fullImgURL,
                new MyImageResponseListener(imageDate) {
                    @Override
                    public void onResponse(Bitmap response) {
                        FileUtils.saveImageToFile(response, context.getFilesDir(), imageDate + ".jpg");
                        dbHelper.insertData(fullTitle, imageDate, "0");

                        if (MainFragment.instance != null) {

                            Cursor cursor = dbHelper.selectByDate(imageDate);

                            while (cursor.moveToNext()) {

                                int _id = cursor.getInt(cursor.getColumnIndex(ImageDataTable._ID));

                                String imageDateCreated = cursor.getString(cursor.getColumnIndex(ImageDataTable.COLUMN_D_C));
                                String fullTitle = cursor.getString(cursor.getColumnIndex(ImageDataTable.COLUMN_TITLE));
                                String rawBool = cursor.getString(cursor.getColumnIndex(ImageDataTable.COLUMN_IS_FAVORITE));

                                boolean bool = rawBool.equals("1");
                                Bitmap bitmap = FileUtils.readImage(appContext.getFilesDir(), imageDateCreated);
                                DataStore dataStore = new DataStore(bitmap, TimeUtils
                                        .getDate(TimeUtils.PATTERN_JSON_DB, imageDateCreated),
                                        fullTitle, bool, _id);


                                MainFragment.instance.dataList.add(0, dataStore);
                                MainFragment.instance.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MainFragment.instance.adapter.insertItem(0);
                                        MainFragment.instance.recyclerView.scrollToPosition(0);
                                    }
                                });
                            }
                        }
                    }
                }, 1024, 1024, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Notification.showNotification(Notification.ERROR, "Error: Check your internet Connection");
                    }
                });

        NetUtils.getRequestQueue(context).add(imageRequest);
    }

    private abstract class MyImageResponseListener implements Response.Listener<Bitmap> {
        public String imageDate;

        public MyImageResponseListener(String imageDate) {
            this.imageDate = imageDate;
        }
    }
}
