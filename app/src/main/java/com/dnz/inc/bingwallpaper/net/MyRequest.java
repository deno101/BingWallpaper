package com.dnz.inc.bingwallpaper.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dnz.inc.bingwallpaper.MainActivity;
import com.dnz.inc.bingwallpaper.UpdateService;
import com.dnz.inc.bingwallpaper.db.DBHelper;
import com.dnz.inc.bingwallpaper.fragments.ImageFragment;
import com.dnz.inc.bingwallpaper.fragments.MainFragment;
import com.dnz.inc.bingwallpaper.utils.FileUtils;
import com.dnz.inc.bingwallpaper.utils.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyRequest {
    private static final String TAG = "MyRequest";

    public void makeAPICall(int n, final Context appContext) {
        final DBHelper dbHelper = new DBHelper(appContext);
        JsonObjectRequest apiRequest = new JsonObjectRequest(NetUtils.getJsonUrl(n), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("images");
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            String imageDate = jsonObject.getString("startdate");

                            boolean hasMatch = false;
                            for (String s : UpdateService.dataInDB) {
                                if (s.equals(imageDate)) {
                                    hasMatch = true;
                                    break;
                                }
                            }

                            if (hasMatch) {
                                Notification.showNotification(Notification.SUCCESS, "Wallpapers upto-date.");
                                return;
                            } else {
                                Notification.showNotification(Notification.SUCCESS, "Updating wallpapers.");
                            }
                            String title = jsonObject.getString("copyright").trim();
                            String fullImgURL = NetUtils.getCoreUrl(jsonObject.getString("url"));

                            getImage(dbHelper, fullImgURL, appContext, imageDate, title);

                            UpdateService.dataInDB.add(imageDate);

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

    private void getImage(final DBHelper dbHelper, final String fullImgURL, final Context context, String imageDate
            , final String fullTitle) {
        ImageRequest imageRequest = new ImageRequest(fullImgURL,
                new MyImageResponseListener(imageDate) {
                    @Override
                    public void onResponse(Bitmap response) {
                        dbHelper.insertData(fullTitle, imageDate, "0");

                        FileUtils.saveImageToFile(response, context.getFilesDir(), imageDate + ".jpg");

                        String[] splited = fullTitle.split("\\(|\\)");

                        String title = splited[0];
                        String copright = splited[1];

                        if (MainActivity.startFragment != null) {
                            try {
                                MainActivity.startFragment.startImageFragment(response, copright, title, this.imageDate);
                                ImageFragment.refreshMain = true;
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
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
