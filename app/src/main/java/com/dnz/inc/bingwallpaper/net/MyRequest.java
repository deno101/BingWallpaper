package com.dnz.inc.bingwallpaper.net;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dnz.inc.bingwallpaper.async.SaveToDb;
import com.dnz.inc.bingwallpaper.db.DBHelper;
import com.dnz.inc.bingwallpaper.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyRequest {

    public void makeAPICall(int n, final Context appContext) {
        final DBHelper dbHelper = new DBHelper(appContext);
        JsonObjectRequest apiRequest = new JsonObjectRequest(NetUtils.getJsonUrl(n), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("images");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String imageDate = jsonObject.getString("startdate");
                                String title = jsonObject.getString("copyright");
                                String fullImgURL = NetUtils.getCoreUrl(jsonObject.getString("url"));

                                new SaveToDb(dbHelper).execute(title, imageDate);

                                getImage(fullImgURL, appContext, imageDate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        NetUtils.getRequestQueue(appContext).add(apiRequest);
    }

    private void getImage(String fullImgURL, final Context context, String imageDate) {
        ImageRequest imageRequest = new ImageRequest(fullImgURL,
                new MyImageResponseListener(imageDate){
                    @Override
                    public void onResponse(Bitmap response) {
                        FileUtils.saveImageToFile(response, context.getFilesDir(), imageDate+".jpg");
                    }
                }, 1024, 1024, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        NetUtils.getRequestQueue(context).add(imageRequest);
    }

    private abstract class MyImageResponseListener implements Response.Listener<Bitmap>{
        public String imageDate;

        public MyImageResponseListener(String imageDate) {
            this.imageDate = imageDate;
        }
    }
}
