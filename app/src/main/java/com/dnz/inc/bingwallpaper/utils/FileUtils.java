package com.dnz.inc.bingwallpaper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static void saveImageToFile(Bitmap image, File dir, String fileName){
        File file = new File(dir, fileName);

        Log.d(TAG, "saveImageToFile: "+ file.toString());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            boolean bool = false;
            try {
                bool = file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (bool){
                saveImageToFile(image, dir, fileName);
            }else {
                throw new IllegalStateException("Failed to create image file");
            }
        }
    }

    public static Bitmap readImage(File path, String filename){
        path = new File(path, filename+".jpg");
        Bitmap image = null;
        try {
            FileInputStream fin  = new FileInputStream(path);
            image = BitmapFactory.decodeStream(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return image;
    }
}
